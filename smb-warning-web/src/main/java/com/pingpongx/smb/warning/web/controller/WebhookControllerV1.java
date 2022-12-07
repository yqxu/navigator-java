package com.pingpongx.smb.warning.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pingpongx.flowmore.cloud.base.commom.utils.PPConverter;
import com.pingpongx.flowmore.cloud.base.server.annotation.NoAuth;
import com.pingpongx.smb.warning.web.constant.WarnConstants;
import com.pingpongx.smb.warning.web.dao.JiraInfoDao;
import com.pingpongx.smb.warning.web.module.JiraChangelog;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/07/10:23 上午
 * @Description: 回调请求
 * @Version:
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = WarnConstants.BASE_PATH + "/smb/webhook")
public class WebhookControllerV1 {
    private final JiraInfoDao jiraInfoDao;

    @ApiOperation("客户服务-消息回调通知")
    @NoAuth(isPack = false)
    @PostMapping(value = "/robots/dingtalk/customer/service")
    public void customerServiceRobotsCallBack(@RequestBody(required = false) JSONObject json) {
        log.info("客户服务-消息回调内容 {}", JSON.toJSONString(json));
    }

    @ApiOperation("jira-消息回调通知")
    @PostMapping("/jira/hook/customer/service")
    @NoAuth(isPack = false)
    public void customerServiceJiraHook(@RequestBody String message,
                                        @RequestParam("issueId") String issueId,
                                        @RequestParam("issueKey") String issueKey,
                                        @RequestParam("projectId") String projectId,
                                        @RequestParam("projectKey") String projectKey) {
        log.info("客户服务-jira消息回调内容 issueId= {}, issueKey = {}, projectId={}, projectKey ={}", issueId, issueKey, projectId, projectKey);
        if (!"RECALL".equals(projectKey)) {
            return;
        }
        Map<String, Object> stringObjectMap = PPConverter.toObject(message, new TypeReference<JSONObject>() {
        });
        JiraChangelog jiraChangelog = PPConverter.toObject(PPConverter.toJsonString(stringObjectMap.get("changelog")), JiraChangelog.class);
        log.info("jiraChangelog = {}", PPConverter.toJsonStringIgnoreException(jiraChangelog));
        if (jiraChangelog == null || jiraChangelog.getItems() == null || jiraChangelog.getItems().isEmpty()) {
            return;
        }
        for (JiraChangelog.Item item : jiraChangelog.getItems()) {
            if ("status".equals(item.getField())) {

                jiraInfoDao.updateStatus(item.getToString(), issueId, projectKey);
            }
        }
    }

}
