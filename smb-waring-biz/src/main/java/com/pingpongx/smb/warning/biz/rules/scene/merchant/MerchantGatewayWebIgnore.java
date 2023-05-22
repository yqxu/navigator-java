package com.pingpongx.smb.warning.biz.rules.scene.merchant;

import com.pingpongx.smb.export.module.ConfiguredStrRule;
import com.pingpongx.smb.rule.routers.operatiors.StrEquals;
import com.pingpongx.smb.warning.biz.alert.model.MerchantAlert;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MerchantGatewayWebIgnore extends ConfiguredStrRule {
    private static String except = "merchant-gateway-web";

    @PostConstruct
    public void init(){
        this.setType(MerchantAlert.class.getName());
        this.setAttr("application");
        this.setOperation(StrEquals.getInstance(MerchantAlert.class.getSimpleName(),this.dependsAttr()));
        this.setExpected(except);
    }
}
