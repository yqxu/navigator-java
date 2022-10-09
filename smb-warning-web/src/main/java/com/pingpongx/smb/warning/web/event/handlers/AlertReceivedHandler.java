package com.pingpongx.smb.warning.web.event.handlers;


import com.pingpongx.smb.warning.biz.alert.counter.CountContext;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.moudle.Trie;
import com.pingpongx.smb.warning.biz.rules.MatchResult;
import com.pingpongx.smb.warning.biz.rules.RuleTrie;
import com.pingpongx.smb.warning.web.event.AlertReceived;
import com.pingpongx.smb.warning.web.event.CountDone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class AlertReceivedHandler implements ApplicationListener<AlertReceived> {
    @Autowired
    RuleTrie ruleTrie;
    @Autowired
    ApplicationContext applicationContext;

    /****
     * 收到告警后第一个filter 用于计数
     * 抛出 CountDone
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(AlertReceived event) {
        ThirdPartAlert alert = event.getAlert();
        MatchResult result = ruleTrie.match(alert);
        result.getMatchedData().values().stream().map(Map::values)
                .flatMap(c->c.stream()).filter(handler -> handler instanceof CountContext)
                .forEach(handler -> handler.handleMatchedData(alert,result));
        CountDone countDone = new CountDone(applicationContext,alert,result);
        applicationContext.publishEvent(countDone);
    }
}
