package com.pingpongx.smb.warning.biz.rules.dubbo.timeout;

import com.pingpongx.smb.rule.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.rules.ConfiguredLeafRule;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StrExcept2 extends ConfiguredLeafRule {
    private static String except = "No provider available from";

    @PostConstruct
    void init(){
        this.setType(SlsAlert.class);
        this.setAttr("content");
        this.setOperation(StringContains.getInstance(SlsAlert.class.getSimpleName(),this.dependsAttr()));
        this.setExpected(except);
    }

}
