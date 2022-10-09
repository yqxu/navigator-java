package com.pingpongx.smb.warning.biz.rules.dubbo.timeout;

import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.rules.ConfiguredLeafRule;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StrExcept3 extends ConfiguredLeafRule {

    private static String except = "Invoke remote method timeout. method: ";

    @PostConstruct
    void init(){
        this.setType(SlsAlert.class.getTypeName());
        this.setAttr("content");
        this.setOperation(StringContains.getInstance());
        this.setExcepted(except);
    }
}
