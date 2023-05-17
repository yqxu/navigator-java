package com.pingpongx.smb.warning.biz.rules.dubbo.timeout;

import com.pingpongx.smb.export.module.ConfiguredStrRule;
import com.pingpongx.smb.rule.routers.operatiors.StrEquals;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AppName extends ConfiguredStrRule {
    private static String except = "business-gateway";

    @PostConstruct
    public void init(){
        this.setType(SlsAlert.class.getName());
        this.setAttr("appName");
        this.setOperation(StrEquals.getInstance(SlsAlert.class.getSimpleName(),this.dependsAttr()));
        this.setExpected(except);
    }
}
