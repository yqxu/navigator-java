package com.pingpongx.smb.warning.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.pingpongx.flowmore.cloud.base.server.annotation.NoAuth;
import com.pingpongx.smb.warning.api.dto.DingDReceiverDTO;
import com.pingpongx.smb.warning.api.dto.DingDingReceiverDTO;
import com.pingpongx.smb.warning.api.dto.JiraDTO;
import com.pingpongx.smb.warning.api.service.BusinessAlertService;
import com.pingpongx.smb.warning.biz.alert.InhibitionFactory;
import com.pingpongx.smb.warning.biz.alert.ThresholdAlertConf;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.InhibitionResultEnum;
import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;
import com.pingpongx.smb.warning.biz.moudle.dingding.AlertsRequest;
import com.pingpongx.smb.warning.biz.moudle.dingding.FireResults;
import com.pingpongx.smb.warning.biz.rules.BizExceptionRule;
import com.pingpongx.smb.warning.biz.rules.DubbleTimeOut;
import com.pingpongx.smb.warning.web.convert.Convert;
import com.pingpongx.smb.warning.web.helper.BusinessAlertHelper;
import com.pingpongx.smb.warning.web.module.FireResultInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/06/7:10 下午
 * @Description: 钉钉业务告警监控通知
 * @Version:
 */

@Slf4j
@RestController
@RequestMapping("/v2/alert")
@RequiredArgsConstructor
public class AlertController {
    private final BusinessAlertHelper businessAlertHelper;
    private final BusinessAlertService businessAlertService;
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

    @PostMapping("/{depart}")
    @NoAuth(isPack = false)
    public DingDingReceiverDTO createAlertWorkOrder(@PathVariable("depart") String depart, @RequestBody String message) {
        try {
            SlsAlert slsAlert = JSON.parseObject(message, SlsAlert.class);

            log.info("request:\n"+message);
        } catch (Exception ex) {
            log.warn("业务告警解析异常!", ex);
        }
        return DingDingReceiverDTO.defaultReceiver();
    }

    @PostMapping("/jirahook")
    @NoAuth(isPack = false)
    public void jiraHook(@RequestBody String message) {
        log.info("jiraHook message:[{}]", message);
        try {
            JiraDTO jiraDTO = Convert.parseJiraDTO(message);
            businessAlertHelper.sendDingTalkMarkDown(jiraDTO);
        } catch (Exception e) {
            log.warn("[jiraHook] error, message [{}]", message, e);
        }
    }
}
