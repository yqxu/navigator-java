package com.pingpongx.smb.warning.biz.alert.event;

import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.rules.MatchResult;
import org.springframework.context.ApplicationEvent;

public class CountDone  extends ApplicationEvent {
    MatchResult context;
    ThirdPartAlert alert;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public CountDone(Object source, ThirdPartAlert alert, MatchResult context) {
        super(source);
        this.alert = alert;
        this.context = context;
    }

    public ThirdPartAlert getAlert() {
        return this.alert;
    }
}
