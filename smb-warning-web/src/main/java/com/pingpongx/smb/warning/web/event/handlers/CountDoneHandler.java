package com.pingpongx.smb.warning.web.event.handlers;


import com.pingpongx.smb.warning.biz.alert.event.CountDone;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.rules.MatchResult;
import com.pingpongx.smb.warning.biz.rules.RuleTrie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class CountDoneHandler implements ApplicationListener<CountDone> {
    @Autowired
    RuleTrie ruleTrie;
    @Autowired
    ApplicationContext applicationContext;


    @Override
    public void onApplicationEvent(CountDone event) {
        ThirdPartAlert alert = event.getAlert();
        MatchResult result = ruleTrie.match(alert);
        result.getMatchedData().values().stream().map(Map::values)
                .flatMap(c->c.stream()).filter(handler -> handler instanceof Inhibition)
                .forEach(handler -> handler.handleMatchedData(alert,result));
    }
}
