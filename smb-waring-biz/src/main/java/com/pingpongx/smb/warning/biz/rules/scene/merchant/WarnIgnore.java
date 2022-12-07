package com.pingpongx.smb.warning.biz.rules.scene.merchant;

import com.pingpongx.smb.rule.routers.operatiors.StrEquals;
import com.pingpongx.smb.rule.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.alert.model.MerchantAlert;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.rules.ConfiguredLeafRule;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class WarnIgnore extends ConfiguredLeafRule {
    private static String except = "[WARN]";

    @PostConstruct
    public void init(){
        this.setType(MerchantAlert.class);
        this.setAttr("content");
        this.setOperation(StringContains.getInstance(MerchantAlert.class.getSimpleName(),this.dependsAttr()));
        this.setExpected(except);
    }
}
