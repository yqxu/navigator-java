package com.pingpongx.smb.warning.web.event.handlers;


import com.pingpongx.smb.warning.api.dto.DingDReceiverDTO;
import com.pingpongx.smb.warning.api.dto.DingDingReceiverDTO;
import com.pingpongx.smb.warning.api.request.JiraGenerateRequest;
import com.pingpongx.smb.warning.api.service.BusinessAlertService;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.depends.DingTalkClientFactory;
import com.pingpongx.smb.warning.biz.depends.PPDingTalkClient;
import com.pingpongx.smb.warning.biz.rules.RuleTrie;
import com.pingpongx.smb.warning.web.event.ToExecute;
import com.pingpongx.smb.warning.web.event.ToInhibition;
import com.pingpongx.smb.warning.web.parser.AlertParser;
import com.pingpongx.smb.warning.web.parser.ParserFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ToInhibitionHandler implements ApplicationListener<ToInhibition> {
    @Autowired
    RuleTrie ruleTrie;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    DingTalkClientFactory dingTalkClientFactory;

    @Autowired
    ParserFactory parserFactory;

    @Autowired
    BusinessAlertService businessAlertService;


    @Override
    public void onApplicationEvent(ToInhibition event) {
        ThirdPartAlert alert = event.getAlert();
        log.info("Alert has been Inhibited.\n"+ alert.throwContent());
    }
}
