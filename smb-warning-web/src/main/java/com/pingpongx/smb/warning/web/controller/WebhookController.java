package com.pingpongx.smb.warning.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pingpongx.flowmore.cloud.base.server.annotation.NoAuth;
import com.pingpongx.smb.warning.api.dto.JiraDTO;
import com.pingpongx.smb.warning.biz.util.JiraUtils;
import com.pingpongx.smb.warning.web.helper.BusinessAlertHelper;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/07/10:23 上午
 * @Description: 回调请求
 * @Version:
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/smb/webhook")
public class WebhookController {

    private final BusinessAlertHelper businessAlertHelper;

    @ApiOperation("jira-消息回调通知")
    @PostMapping("/jirahook")
    @NoAuth(isPack = false)
    public void jiraHook(@RequestBody String message) {
        log.info("jiraHook message:[{}]", message);
        try {
            JiraDTO jiraDTO = JiraUtils.parseJiraDTO(message);
            businessAlertHelper.sendDingTalkMarkDown(jiraDTO);
        } catch (Exception e) {
            log.warn("[jiraHook] error, message [{}]", message, e);
        }
    }

    @ApiOperation("钉钉机器人-消息回调通知")
    @NoAuth(isPack = false)
    @PostMapping(value = "/robots/dingtalk")
    public void robotsCallBack(@RequestBody(required = false) JSONObject json) {
        log.info("{}", JSON.toJSONString(json));
        String content = json.getJSONObject("text").getString("content");
        String userId = json.get("senderStaffId").toString();
        log.info("{}", content);
        log.info("{}", userId);
    }


}
