package com.pingpongx.smb.warning.web.helper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.pingpongx.smb.warning.api.dto.DingDingReceiverDTO;
import com.pingpongx.smb.warning.api.dto.JiraDTO;
import com.pingpongx.smb.warning.api.request.JiraGenerateRequest;
import com.pingpongx.smb.warning.api.service.BusinessAlertService;
import com.pingpongx.smb.warning.biz.constants.BusinessAlertProperty;
import com.pingpongx.smb.warning.biz.redis.BusinessAlertsRedisHelper;
import com.pingpongx.smb.warning.biz.service.DingTalkService;
import com.pingpongx.smb.warning.web.module.AlertUser;
import com.pingpongx.smb.warning.web.module.FireResultInfo;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/06/8:02 下午
 * @Description:
 * @Version:
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BusinessAlertHelper {

    private final BusinessAlertProperty property;
    private final BusinessAlertService businessAlertService;
    private final BusinessAlertsRedisHelper businessAlertsRedisHelper;


    private final LongAdder LONG_ADDER = new LongAdder();
    private final ExecutorService EXECUTOR = new ThreadPoolExecutor(10, 10,
        10L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(10000), r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        LONG_ADDER.increment();
        thread.setName("BusinessAlertHelper-thread:" + LONG_ADDER.intValue());
        return thread;
    });

    public final LoadingCache<String, DingDingReceiverDTO> APP_DINGDING_RECEIVER = CacheBuilder.newBuilder()
        .initialCapacity(50)
        .expireAfterAccess(12, TimeUnit.HOURS)
        .maximumSize(500)
        .build(new CacheLoader<String, DingDingReceiverDTO>() {
            @Override
            public DingDingReceiverDTO load(String key) throws Exception {
                return businessAlertService.findDingDingReceivers(key);
            }
        });

    /**
     * 超时告警通知
     * @param fireResultInfo 告警请求数据
     */
    public void timeOutAlertNotify(FireResultInfo fireResultInfo,List<String> phoneList){
        String summary = StringUtils.substring(fireResultInfo.getContent(), 0, 300);
        if (businessAlertsRedisHelper.needSendDingDing(fireResultInfo.getAppName(), summary)) {
            return;
        }
        new DingTalkService(property.getNotify_time_out_dingTalk_url()).sendDingTalkText(
            buildTimeOutDingDingContent(fireResultInfo.getAppName(),summary,fireResultInfo.getTraceId(),phoneList), phoneList);
    }

    /**
     * 发送钉钉消息
     *
     * @param jiraDTO jira消息对象
     */
    public void sendDingTalkMarkDown(JiraDTO jiraDTO) {
        String title = "温馨提示：新的JIRA工单消息";
        AlertUser dealt = new AlertUser();
        dealt.setCallingPrefix("+86");
        dealt.setPhone("15757115779");
//        String phone = ALERT_USER_CACHE.getOrDefault(jiraDTO.getAssignee(), dealt).getPhone();
        String phone = "ALERT_USER_CACHE.getOrDefault(jiraDTO.getAssignee(), dealt).getPhone()";
        jiraDTO.setAssignee(phone);
        new DingTalkService(property.getNotify_jira_change_dingTalk_url()).sendDingTalkMarkDown(title, buildDingTalkContent(jiraDTO), Arrays.asList(phone));
    }

    private String buildDingTalkContent(JiraDTO jiraDTO) {
        StringBuilder content = new StringBuilder().append(" #### 温馨提示：新的JIRA工单消息\n\n")
            .append("**报告人：**  ").append(jiraDTO.getReporter()).append("\n\n")
            .append("**经办人：**  @")
            .append(jiraDTO.getAssignee()).append("\n\n").append("**状态：**  ")
            .append(jiraDTO.getStatus()).append("\n\n")
            .append("**问题来源：**\t").append(jiraDTO.getIssueType()).append("\n\n")
            .append("**摘要信息：**\n\n").append("> ").append(jiraDTO.getSummary()).append("\n\n");
        if (StringUtils.isNotEmpty(jiraDTO.getReason())) {
            content.append("**问题原因：**\n\n").append("> ").append(jiraDTO.getReason()).append("\n\n");
        }
        if (StringUtils.isNotEmpty(jiraDTO.getAction())) {
            content.append("**解决方案：**\n\n").append("> ").append(jiraDTO.getAction()).append("\n\n");
        }
        content.append("------\n\n").append("**JIRA链接：**  [查看详情](").append(jiraDTO.getUrl()).append(")");
        return content.toString();
    }

    private String buildTimeOutDingDingContent(String appName, String summary, String traceId, List<String> phoneList) {
        String[] timeOutAlertTimeInterval = property.getTimeOutAlertTimeInterval();
        StringBuilder sbr = new StringBuilder();
        sbr.append("超时告警提醒\n\n");
        sbr.append("应用名：").append(appName).append("\n");
        sbr.append("摘要信息：").append(summary).append("\n");
        sbr.append("traceId：").append(traceId).append("\n");
        sbr.append(timeOutAlertTimeInterval[1]).append("分钟内请求次数").append(timeOutAlertTimeInterval[0]).append("次\n");
        sbr.append("应用负责人：").append(phoneList.stream().map(item -> "@" + item).collect(Collectors.joining(","))).append("\n\n");
        return sbr.toString();
    }

    public void asyncCreateJiraOrder(FireResultInfo fireResultInfo) {
        String summary = StringUtils.substring(fireResultInfo.getContent(), 0, 200);
        if (!canPass(fireResultInfo) || businessAlertsRedisHelper.hasCreateJiraByCache(fireResultInfo.getAppName(), summary)) {
            return;
        }
        EXECUTOR.execute(() -> {
            try {
                JiraGenerateRequest build = JiraGenerateRequest.builder()
                    .appName(fireResultInfo.getAppName())
                    .traceId(StringUtils.defaultIfBlank(fireResultInfo.getTraceId(),"#"))
                    .summary(summary)
                    .description(fireResultInfo.getContent())
                    .build();
                businessAlertService.generateJira(build);
            }catch(Exception ex){
                log.warn("异步创建jira工单失败!失败原因:",ex);
            }
        });
    }

    private boolean canPass(FireResultInfo fireResultInfo) {
        return null != fireResultInfo && !StringUtils.isBlank(fireResultInfo.getAppName())
            && !StringUtils.isBlank(fireResultInfo.getContent());
    }
}
