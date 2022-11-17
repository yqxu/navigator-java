package com.pingpongx.smb.warning.biz.rules.dubbo.timeout;

import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.StrEquals;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.rules.ConfiguredLeafRule;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AppName extends ConfiguredLeafRule {
    private static String except = "business-gateway";

    @PostConstruct
    void init(){
        this.setType(SlsAlert.class.getSimpleName());
        this.setAttr("appName");
        this.setOperation(StrEquals.getInstance());
        this.setExpected(except);
    }
}
