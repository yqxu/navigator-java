package com.pingpongx.smb.warning.biz.rules.bizexp;

import com.pingpongx.smb.rule.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.alert.model.MerchantAlert;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.rules.ConfiguredLeafRule;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BizExpRule3 extends ConfiguredLeafRule {
    private static String except = "[1021]店铺提现金额不满足";

    @PostConstruct
    void init(){
        this.setType(MerchantAlert.class);
        this.setAttr("content");
        this.setOperation(StringContains.getInstance(MerchantAlert.class.getSimpleName(),this.dependsAttr()));
        this.setExpected(except);
    }
}
