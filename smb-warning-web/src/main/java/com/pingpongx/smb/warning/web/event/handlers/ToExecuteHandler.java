package com.pingpongx.smb.warning.web.event.handlers;


import com.pingpongx.smb.warning.api.dto.DingDReceiverDTO;
import com.pingpongx.smb.warning.api.dto.DingDingReceiverDTO;
import com.pingpongx.smb.warning.api.dto.JiraDTO;
import com.pingpongx.smb.warning.api.request.JiraGenerateRequest;
import com.pingpongx.smb.warning.api.service.BusinessAlertService;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.depends.DingTalkClientFactory;
import com.pingpongx.smb.warning.biz.depends.PPDingTalkClient;
import com.pingpongx.smb.warning.biz.rules.MatchResult;
import com.pingpongx.smb.warning.biz.rules.RuleTrie;
import com.pingpongx.smb.warning.web.event.CountDone;
import com.pingpongx.smb.warning.web.event.ToExecute;
import com.pingpongx.smb.warning.web.parser.AlertParser;
import com.pingpongx.smb.warning.web.parser.ParserFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ToExecuteHandler implements ApplicationListener<ToExecute> {
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
    public void onApplicationEvent(ToExecute event) {
        //DingDing 通知
        ThirdPartAlert alert = event.getAlert();
        PPDingTalkClient client = dingTalkClientFactory.getByDepart(event.getDepart());
        AlertParser parser = parserFactory.departOf(event.getDepart());
        String msg = parser.toDingTalkMsg(alert);
        DingDingReceiverDTO receiverDTO = businessAlertService.findDingDingReceivers(alert.throwAppName());
        List<String> phones = receiverDTO.getReceivers().stream().map(DingDReceiverDTO::getPhone).collect(Collectors.toList());
        client.sendMarkDown("您有来自线上的预警待处理",msg,phones);
        //Jira 工单
        JiraGenerateRequest req = parser.generateJiraRequest(alert);
        businessAlertService.generateJira(req);
    }
}
