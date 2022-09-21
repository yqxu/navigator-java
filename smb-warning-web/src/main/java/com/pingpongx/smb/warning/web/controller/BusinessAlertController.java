package com.pingpongx.smb.warning.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.pingpongx.flowmore.cloud.base.server.annotation.NoAuth;
import com.pingpongx.smb.warning.api.dto.DingDReceiverDTO;
import com.pingpongx.smb.warning.api.dto.DingDingReceiverDTO;
import com.pingpongx.smb.warning.api.service.BusinessAlertService;
import com.pingpongx.smb.warning.biz.alert.InhibitionFactory;
import com.pingpongx.smb.warning.biz.alert.ThresholdAlertConf;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.InhibitionResultEnum;
import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;
import com.pingpongx.smb.warning.biz.moudle.dingding.AlertsRequest;
import com.pingpongx.smb.warning.biz.moudle.dingding.FireResults;
import com.pingpongx.smb.warning.biz.rules.BizExceptionRule;
import com.pingpongx.smb.warning.biz.rules.DubbleTimeOut;
import com.pingpongx.smb.warning.web.helper.BusinessAlertHelper;
import com.pingpongx.smb.warning.web.module.FireResultInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/06/7:10 下午
 * @Description: 钉钉业务告警监控通知
 * @Version:
 */

@Slf4j
@RestController
@RequestMapping("/v1/smb/dingDing")
@RequiredArgsConstructor
public class BusinessAlertController {

    private final BusinessAlertHelper businessAlertHelper;
    private final BusinessAlertService businessAlertService;

    public static void main(String[] args){
        FireResults test = new FireResults();
        test.setContent("dfsafdaa No provider available fromfdpsaojfoipans");
        inhibition.needInhibition(test);
    }
    private static Inhibition<FireResults> inhibition = InhibitionFactory.getInhibition(new ThresholdAlertConf<>(5,TimeUnit.Minutes,10,10,FireResults.class),new DubbleTimeOut());
    private static Inhibition<FireResults> inhibitionBizExp = InhibitionFactory.getInhibition(new ThresholdAlertConf<>(5,TimeUnit.Minutes,10,10,FireResults.class),new BizExceptionRule());

    List<Inhibition<FireResults>> inhibitions = new ArrayList<>();

    @PostConstruct
    void init(){
        inhibitions.add(inhibition);
        inhibitions.add(inhibitionBizExp);
    }

    private InhibitionResultEnum needInhibition(FireResults fireResults){
        return inhibitions.parallelStream().map(inhibition->inhibition.needInhibition(fireResults))
                .reduce((enum1,enum2)->enum1.getLevel()>enum2.getLevel()?enum1:enum2)
                .orElse(InhibitionResultEnum.UnMatched);
    }

    @PostMapping("/businessAlerts")
    @NoAuth(isPack = false)
    public DingDingReceiverDTO findDingDingReceivers(HttpServletRequest request, @RequestBody AlertsRequest alertsRequest){
        try {
            // 调试日志测试无误可删除
            log.info("AlertController.findDingDingReceivers 请求入参信息:[{}],alertsRequest:[{}]", JSONObject.toJSONString(request.getHeaderNames()),JSONObject.toJSONString(alertsRequest));
            // 调试日志测试无误可删除
            FireResults fireResults = alertsRequest.getAlerts().get(0).getFire_results().get(0);
            //TODO 抑制策略较多较复杂时需要在rete下编排规则，暂时写死，rete第一版简单用trie实现
            InhibitionResultEnum inhibitResult = needInhibition(fireResults);
            if (inhibitResult.isNeedInhibition()){
                log.info("告警被抑制：\n"+fireResults);
                return null;
            }
            if (inhibitResult.equals(InhibitionResultEnum.MatchedAndNeedInhibition)){
                log.error("告警被抑制后,超出阈值依然抛出：\n"+fireResults.getContent());
            }
            String appName = Optional.ofNullable(fireResults.getAppName()).orElse(fireResults.get_container_name_());
            return businessAlertHelper.APP_DINGDING_RECEIVER.get(appName);
        } catch (Exception ex) {
            log.warn("钉钉告警签名发生改变请及时配合更新!", ex);
            return DingDingReceiverDTO.builder().receivers(Lists.newArrayList(DingDReceiverDTO.getDefaultUser())).build();
        }
    }

    @GetMapping("/clearCache/{appName}")
    @NoAuth(isPack = false)
    public void clearCache(@PathVariable("appName") String appName){
        if (StringUtils.isNotBlank(appName)) {
            businessAlertService.clearCache(Lists.newArrayList(appName));
            businessAlertHelper.APP_DINGDING_RECEIVER.refresh(appName);
        } else if ("all".equals(appName)){
            businessAlertService.clearCache();
            businessAlertHelper.APP_DINGDING_RECEIVER.cleanUp();
        }
    }

    /**
     * 创建告警工单
     * @param resultInfo 请求体
     */
    @PostMapping("/createAlertWorkOrder")
    @NoAuth(isPack = false)
    public DingDingReceiverDTO createAlertWorkOrder(@RequestBody FireResultInfo resultInfo) {
        try {
            String appName = parsingAppName(resultInfo.getAppName());
            if (StringUtils.isEmpty(appName)) {
                return DingDingReceiverDTO.defaultReceiver();
            }
            resultInfo.setAppName(appName);
            if (StringUtils.isNotEmpty(resultInfo.getMessage())) {
                resultInfo.setContent(resultInfo.getMessage());
            }
            resultInfo.setContent(replaceBlank(resultInfo.getContent()));
            businessAlertHelper.asyncCreateJiraOrder(resultInfo);
            return businessAlertHelper.APP_DINGDING_RECEIVER.get(resultInfo.getAppName());
        } catch (Exception ex) {
            log.warn("业务告警解析异常!", ex);
        }
        return DingDingReceiverDTO.defaultReceiver();
    }

    /**
     * 超时告警
     * @param resultInfo 告警信息
     */
    @PostMapping("/timeOutAlert")
    @NoAuth(isPack = false)
    public void timeOutAlert(@RequestBody FireResultInfo resultInfo) {
        try {
            resultInfo.setAppName(parsingAppName(resultInfo.getAppName()));
            DingDingReceiverDTO orDefault = businessAlertHelper.APP_DINGDING_RECEIVER.get(resultInfo.getAppName());
            resultInfo.setContent(replaceBlank(resultInfo.getContent()));
            businessAlertHelper.timeOutAlertNotify(resultInfo, orDefault.getReceivers().stream().map(DingDReceiverDTO::getPhone).collect(Collectors.toList()));
        } catch (Exception ex) {
            log.warn("超时业务告警解析异常!", ex);
        }
    }

    private String parsingAppName(String appName) {
        if (appName.contains("null")) {
            appName = StringUtils.remove(appName, "null").trim();
        }
        if (appName.contains("efficiency")) {
            appName = StringUtils.substringBefore(appName,"efficiency");
        }
        return replaceBlank(appName);
    }

    private String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

}
