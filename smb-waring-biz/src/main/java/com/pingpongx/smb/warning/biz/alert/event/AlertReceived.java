package com.pingpongx.smb.warning.biz.alert.event;

import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import org.springframework.context.ApplicationEvent;

public class AlertReceived extends ApplicationEvent {
    ThirdPartAlert alert;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public AlertReceived(Object source) {
        super(source);
    }

    public ThirdPartAlert getAlert() {
        return alert;
    }

    public void setAlert(ThirdPartAlert alert) {
        this.alert = alert;
    }
}
