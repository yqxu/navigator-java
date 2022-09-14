package com.pingpongx.smb.warning.biz.impl.dingding;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HtmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pingpongx.smb.warning.api.dto.AlertUserDTO;
import com.pingpongx.smb.warning.api.dto.BusinessAlertsAppDTO;
import com.pingpongx.smb.warning.api.dto.DingDReceiverDTO;
import com.pingpongx.smb.warning.api.dto.DingDingReceiverDTO;
import com.pingpongx.smb.warning.api.request.JiraGenerateRequest;
import com.pingpongx.smb.warning.api.service.BusinessAlertService;
import com.pingpongx.smb.warning.biz.cache.BusinessAlertsCache;
import com.pingpongx.smb.warning.biz.constants.JiraConsts;
import com.pingpongx.smb.warning.biz.util.BeanUtils;
import com.pingpongx.smb.warning.biz.util.ConvertUtil;
import com.pingpongx.smb.warning.biz.util.HttpAPI;
import com.pingpongx.smb.warning.biz.util.HttpResult;
import com.pingpongx.smb.warning.dal.dataobject.BusinessAlertsApp;
import com.pingpongx.smb.warning.dal.dataobject.BusinessAlertsUser;
import com.pingpongx.smb.warning.dal.dataobject.BusinessAlertsUserMap;
import com.pingpongx.smb.warning.dal.repository.BusinessAlertsAppRepository;
import com.pingpongx.smb.warning.dal.repository.BusinessAlertsUserRepository;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/06/7:56 下午
 * @Description:
 * @Version:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessAlertServiceImpl implements BusinessAlertService {

    private final BusinessAlertsUserRepository businessAlertsUserRepository;

    private final BusinessAlertsAppRepository businessAlertsAppRepository;

    private final BusinessAlertsCache businessAlertsCache;

    @Value("${business.jira.token:MTAwODgyMDk4MTY3OuZqSyEfQLeo/IFKtYhwSudgjme0}")
    private String jiraToken;

    @Value("${business.jira.app-test.dict:{\"SMB-福贸\":\"fuww\", \"SMB-B2B\":\"xuhy1\"}}")
    private String jiraAppTestDict;

    // 小量不改动本地缓存
    private static final Map<String, BusinessAlertsAppDTO> APP_DICT = Maps.newHashMap();

    /**
     * 获取应用告警负责人数据
     * @param appName 应用名
     * @return 告警负责人
     */
    @Override
    public DingDingReceiverDTO findDingDingReceivers(String appName) {
        log.info("BusinessAlertServiceImpl.findDingDingReceivers#查询告警应用:[{}]", appName);
        //考虑性能问题数据从缓存取
        List<BusinessAlertsUserMap> alertUserCache = businessAlertsCache.findByAppName(appName);
        if (null == alertUserCache) {//通知默认负责人
            return DingDingReceiverDTO.builder().receivers(Lists.newArrayList(businessAlertsCache.defaultReceiver())).build();
        }
        List<DingDReceiverDTO> receiverDTOList = alertUserCache.stream()
            .map(alertsUser -> DingDReceiverDTO.builder().code(alertsUser.getCallingPrefix()).phone(alertsUser.getPhone()).email(alertsUser.getEmail())
                .domainAccount(alertsUser.getDomainAccount())
                .build())
            .collect(Collectors.toList());
        return DingDingReceiverDTO.builder().receivers(receiverDTOList).build();
    }

    @SneakyThrows
    @Override
    public void generateJira(JiraGenerateRequest request) {
        request.check();
        String req = buildRequest(request).toJSONString();
        Map<String, Object> headers = Maps.newHashMap();
        headers.put(HttpHeaders.AUTHORIZATION, "Bearer " + jiraToken);
        HttpResult httpResult = new HttpAPI().postJson(JiraConsts.JIRA_ISSUE_URL, headers, req);
        log.info("[BusinessAlertServiceImpl.generateJira] request [{}], HttpResult [{}]", req, httpResult);
    }

    private JSONObject buildRequest(JiraGenerateRequest request) {
        String appName = request.getAppName();
        String domainAccount = findDingDingReceivers(appName).getReceivers().get(0).getDomainAccount();
        String summary = CharSequenceUtil.removeAllLineBreaks(StringEscapeUtils.unescapeJava(HtmlUtil.filter(request.getSummary())));
        summary = StringUtils.substring(summary, 0, 200);
        String businessLine = Optional.ofNullable(getAppDTO(appName)).map(BusinessAlertsAppDTO::getClassify).orElse("SMB-B2B");
        String testName = (String) JSON.parseObject(jiraAppTestDict).getOrDefault(businessLine, "fuww");
        JSONObject req = new JSONObject();
        JSONObject fields = new JSONObject();
        req.put("fields", fields);
        JSONObject project = new JSONObject();
        project.put("key", "TICKET");
        fields.put("project", project);
        // 保存版本号
        fields.put("fixVersions", this.getVersions());
        JSONObject issuetype = new JSONObject();
        issuetype.put("name", "SMB线上预警");
        fields.put("issuetype", issuetype);
        fields.put("customfield_11200", request.getTraceId());
        // 经办人
        JSONObject assignee = new JSONObject();
        assignee.put("name", domainAccount);
        fields.put("assignee", assignee);
        // 处理人
        JSONObject customfield_10700 = new JSONObject();
        customfield_10700.put("name", domainAccount);
        fields.put("customfield_10700", customfield_10700);
        // 测试人员
        JSONObject customfield_10306 = new JSONObject();
        customfield_10306.put("name", testName);
        JSONArray testArr = new JSONArray();
        testArr.add(customfield_10306);
        fields.put("customfield_10306", testArr);

        fields.put("summary", summary);
        fields.put("description", request.getDescription());
        JSONObject customfield_11209 = new JSONObject();
        fields.put("customfield_11209", customfield_11209);
        customfield_11209.put("value", businessLine);
        JSONObject child = new JSONObject();
        customfield_11209.put("child", child);
        child.put("value", appName);
        return req;
    }

    @Data
    static class JiraVersion implements Serializable {

        private static final long serialVersionUID = 3479768660696172271L;

        private String name;

    }

    @Override
    public void clearCache() {
        businessAlertsCache.allCacheRefresh();
    }

    @Override
    public void clearCache(List<String> appNames) {
        businessAlertsCache.cacheRefreshByAppName(appNames);
    }

    /**
     * 根据当前时间计算版本号
     *
     * @return 当前版本号集合
     */
    private List<Map<String, Object>> getVersions() {
        Date featureDate = DateUtils.addDays(new Date(), 7);
        String format = DateFormatUtils.format(featureDate, "yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT+08:00"));
        String year = StringUtils.substring(format, 2, 4);
        String month = StringUtils.substring(format, 5, 7);
        String weekIdx = DateUtil.weekOfMonth(featureDate) + "";
        // 默认修复版本是当前时间的下周一
        String version = year + month + "." + weekIdx + 1;
        Map<String, Object> objectMap = new HashMap<>();
        Map<String, String> versionMap = new HashMap<>();
        versionMap.put("name", version);
        objectMap.put("add", versionMap);
        return Arrays.asList(objectMap);
    }

    private BusinessAlertsAppDTO getAppDTO(String appName) {
        return APP_DICT.computeIfAbsent(appName, an -> {
            BusinessAlertsApp businessAlertsApp = businessAlertsAppRepository.lambdaQuery()
                .eq(BusinessAlertsApp::getAppName, an)
                .last("limit 1").one();
            return BeanUtils.convert(businessAlertsApp, BusinessAlertsAppDTO.class);
        });
    }



    @Override
    public List<AlertUserDTO> getAllPrincipal() {
        return ConvertUtil.convert(businessAlertsUserRepository.lambdaQuery().list(), AlertUserDTO.class);
    }

    @Override
    public AlertUserDTO getPrincipalByEmail(String email) {
        return ConvertUtil
            .convert(businessAlertsUserRepository.lambdaQuery().eq(BusinessAlertsUser::getEmail, email).last("limit 1").one(), AlertUserDTO.class);
    }
}
