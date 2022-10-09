package com.pingpongx.smb.warning.biz.alert.configuration;

import com.pingpongx.smb.warning.biz.rules.scene.DubbleTimeOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlertAssemble {
    @Autowired
    DubbleTimeOut timeOutRule;

}
