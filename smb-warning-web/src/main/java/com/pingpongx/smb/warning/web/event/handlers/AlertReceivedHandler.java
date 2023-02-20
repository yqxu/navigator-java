package com.pingpongx.smb.warning.web.event.handlers;


import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.MatchResult;
import com.pingpongx.smb.export.module.PipelineContext;
import com.pingpongx.smb.warning.biz.alert.counter.CountContext;
import com.pingpongx.smb.warning.biz.alert.event.AlertReceived;
import com.pingpongx.smb.warning.biz.alert.event.CountDone;
import com.pingpongx.smb.warning.biz.alert.event.ToExecute;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AlertReceivedHandler implements ApplicationListener<AlertReceived> {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    Engine ruleEngine;

    /****
     * 收到告警后第一个filter 用于计数
     * 抛出 CountDone
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(AlertReceived event) {
        ThirdPartAlert alert = event.getAlert();
        MatchResult result = ruleEngine.match(alert.getClass().getName(),alert);
        if (result.getMatchedData().size()==0){
            ToExecute toExecute = new ToExecute(applicationContext,alert);
            applicationContext.publishEvent(toExecute);
            return;
        }
        result.getMatchedData().stream()
                        .filter(handler -> handler instanceof CountContext)
                        .map(handler-> {
                            PipelineContext context = PipelineContext.of(result,((CountContext)handler).getSceneIdentity());
                            handler.handleMatchedData(alert, context);
                            return context;
                        })
                .map(context->new CountDone(applicationContext,alert,context))
                .forEach(countDone -> applicationContext.publishEvent(countDone));
    }
}
