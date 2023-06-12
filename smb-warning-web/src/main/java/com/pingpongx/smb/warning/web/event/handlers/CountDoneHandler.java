package com.pingpongx.smb.warning.web.event.handlers;


import com.pingpongx.smb.export.module.PipelineContext;
import com.pingpongx.smb.export.spi.RuleHandler;
import com.pingpongx.smb.warning.biz.alert.event.CountDone;
import com.pingpongx.smb.warning.biz.alert.event.ToExecute;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CountDoneHandler implements ApplicationListener<CountDone> {

    @Autowired
    ApplicationContext applicationContext;

    private String getContextScene(PipelineContext context){
        return context.getParams().get("scene");
    }

    @Override
    public void onApplicationEvent(CountDone event) {
        ThirdPartAlert alert = event.getAlert();
        PipelineContext context = event.getContext();
        Set<RuleHandler> handlerSet = context.getMatchedHandler().getMatchedData().stream()
                .filter(handler -> handler.getIdentify()!=null)
                .filter(handler -> handler.tags().contains(getContextScene(event.getContext())))
                .filter(handler -> handler instanceof Inhibition).collect(Collectors.toSet());
        if (handlerSet.size() == 0){
            applicationContext.publishEvent(new ToExecute(applicationContext,alert));
            return;
        }
        handlerSet.forEach(handler -> handler.doAction(alert,context));
    }
}
