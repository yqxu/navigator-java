package com.pingpongx.smb.warning.biz.rules.dubbo.timeout;

import com.pingpongx.smb.export.module.ConfiguredStrRule;
import com.pingpongx.smb.rule.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StrExcept4 extends ConfiguredStrRule {
    private static String except = "Tried 1 times of the providers [";

    @PostConstruct
    void init(){
        this.setType(SlsAlert.class.getName());
        this.setAttr("content");
        this.setOperation(StringContains.getInstance(SlsAlert.class.getSimpleName(),this.dependsAttr()));
        this.setExpected(except);
    }
}
