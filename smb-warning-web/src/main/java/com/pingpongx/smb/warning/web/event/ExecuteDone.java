package com.pingpongx.smb.warning.web.event;

import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import org.springframework.context.ApplicationEvent;

public class ExecuteDone  extends ApplicationEvent {
    ThirdPartAlert alert;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ExecuteDone(Object source) {
        super(source);
    }
}
