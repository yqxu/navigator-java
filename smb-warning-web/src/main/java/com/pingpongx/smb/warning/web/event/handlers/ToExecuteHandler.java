package com.pingpongx.smb.warning.web.event.handlers;


import com.pingpongx.smb.warning.api.dto.DingDReceiverDTO;
import com.pingpongx.smb.warning.api.dto.DingDingReceiverDTO;
import com.pingpongx.smb.warning.api.request.JiraGenerateRequest;
import com.pingpongx.smb.warning.api.service.BusinessAlertService;
import com.pingpongx.smb.warning.biz.alert.event.ToExecute;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.depends.DingTalkClientFactory;
import com.pingpongx.smb.warning.biz.depends.PPDingTalkClient;
import com.pingpongx.smb.warning.web.parser.AlertParser;
import com.pingpongx.smb.warning.web.parser.ParserFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ToExecuteHandler implements ApplicationListener<ToExecute> {

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
        return;
//        //DingDing 通知
//        ThirdPartAlert alert = event.getAlert();
//        PPDingTalkClient client = dingTalkClientFactory.getByDepart(alert.depart());
//        AlertParser parser = parserFactory.departOf(alert.depart());
//        String msg = parser.toDingTalkMsg(alert);
//        DingDingReceiverDTO receiverDTO = businessAlertService.findDingDingReceivers(alert.throwAppName());
//        if (receiverDTO == null||receiverDTO.getReceivers()==null){
//            log.error(event.getAlert().throwAppName()+" 未设置对应负责人列表。");
//            return;
//        }
//        List<String> phones = receiverDTO.getReceivers().stream().filter(Objects::nonNull).map(DingDReceiverDTO::getPhone).collect(Collectors.toList());
//        client.sendMarkDown("您有来自线上的预警待处理",msg,phones);
//        if (phones.size() == 0){
//            log.error(event.getAlert().throwAppName()+" 未设置对应负责人列表。");
//        }else {
//            //Jira 工单
//            JiraGenerateRequest req = parser.generateJiraRequest(alert);
//            businessAlertService.generateJira(req);
//        }
    }
}
