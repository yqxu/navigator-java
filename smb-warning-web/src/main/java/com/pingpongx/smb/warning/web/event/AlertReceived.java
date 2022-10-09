package com.pingpongx.smb.warning.web.event;

import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import org.springframework.context.ApplicationEvent;

public class AlertReceived extends ApplicationEvent {
    ThirdPartAlert alert;

    String depart;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public AlertReceived(Object source,String depart) {
        super(source);
        this.depart = depart;
    }

    public ThirdPartAlert getAlert() {
        return alert;
    }

    public String getDepart() {
        return depart;
    }
}
