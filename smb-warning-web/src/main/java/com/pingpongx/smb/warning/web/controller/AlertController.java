package com.pingpongx.smb.warning.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.pingpongx.flowmore.cloud.base.server.annotation.NoAuth;
import com.pingpongx.smb.warning.api.dto.DingDReceiverDTO;
import com.pingpongx.smb.warning.api.dto.DingDingReceiverDTO;
import com.pingpongx.smb.warning.api.dto.JiraDTO;
import com.pingpongx.smb.warning.api.service.BusinessAlertService;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.web.convert.Convert;
import com.pingpongx.smb.warning.web.event.AlertReceived;
import com.pingpongx.smb.warning.web.helper.BusinessAlertHelper;
import com.pingpongx.smb.warning.web.parser.AlertParser;
import com.pingpongx.smb.warning.web.parser.ParserFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final ParserFactory parserFactory;
    private final ApplicationContext context;

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
    public ThirdPartAlert createAlertWorkOrder(@PathVariable("depart") String depart, @RequestBody String message) {
        log.info("msg received:\n"+message);
        AlertParser parser = parserFactory.departOf(depart);
        ThirdPartAlert alert = parser.toAlert(message);
        AlertReceived received = new AlertReceived(context);
        received.setAlert(alert);
        context.publishEvent(received);
        return alert;
    }

}
