package com.pingpongx.smb.warning.biz.rules.scene.b2bgateway;

import com.pingpongx.smb.rule.routers.operatiors.StrEquals;
import com.pingpongx.smb.rule.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.rules.ConfiguredLeafRule;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BusinessGateway extends ConfiguredLeafRule {
    private static String except = "business-gateway";

    @PostConstruct
    public void init(){
        this.setType(SlsAlert.class);
        this.setAttr("appName");
        this.setOperation(StrEquals.getInstance(SlsAlert.class.getSimpleName(),this.dependsAttr()));
        this.setExpected(except);
    }
}
