package com.pingpongx.smb.warning.biz.rules.temp;

import com.pingpongx.smb.export.module.ConfiguredLeafRule;
import com.pingpongx.smb.rule.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TempRule1 extends ConfiguredLeafRule {
    private static String except = "com.pingpongx.business.account.biz.payout.PayoutTemplate.payoutRefund(PayoutTemplate.java:165)";

    @PostConstruct
    void init(){
        this.setType(SlsAlert.class.getName());
        this.setAttr("content");
        this.setOperation(StringContains.getInstance(SlsAlert.class.getSimpleName(),this.dependsAttr()));
        this.setExpected(except);
    }

}
