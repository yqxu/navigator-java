package com.pingpongx.smb.warning.biz.alert.event;

import com.pingpongx.smb.export.module.PipelineContext;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import org.springframework.context.ApplicationEvent;

public class CountDone  extends ApplicationEvent {
    PipelineContext context;
    ThirdPartAlert alert;


    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public CountDone(Object source, ThirdPartAlert alert, PipelineContext context) {
        super(source);
        this.alert = alert;
        this.context = context;
    }

    public ThirdPartAlert getAlert() {
        return this.alert;
    }

    public PipelineContext getContext() {
        return context;
    }

    public void setContext(PipelineContext context) {
        this.context = context;
    }

    public void setAlert(ThirdPartAlert alert) {
        this.alert = alert;
    }

}
