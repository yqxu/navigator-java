package com.pingpongx.smb.warning.web.event.handlers;


import com.pingpongx.smb.warning.api.service.BusinessAlertService;
import com.pingpongx.smb.warning.biz.alert.event.ToInhibition;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.depends.DingTalkClientFactory;
import com.pingpongx.smb.warning.biz.rules.RuleTrie;
import com.pingpongx.smb.warning.web.parser.ParserFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

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
