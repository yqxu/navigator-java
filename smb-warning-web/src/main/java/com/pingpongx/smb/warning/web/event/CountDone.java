package com.pingpongx.smb.warning.web.event;

import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.rules.MatchResult;
import org.springframework.context.ApplicationEvent;

public class CountDone  extends ApplicationEvent {
    ThirdPartAlert alert;

    MatchResult matchContext;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public CountDone(Object source,ThirdPartAlert alert,MatchResult matchContext) {
        super(source);
        this.alert = alert;
        this.matchContext = matchContext;
    }

    public ThirdPartAlert getAlert() {
        return alert;
    }

    public MatchResult getMatchContext() {
        return matchContext;
    }
}
