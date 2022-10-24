package com.pingpongx.smb.warning.biz.rules.temp;

import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.rules.ConfiguredLeafRule;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TempRule1 extends ConfiguredLeafRule {
    private static String except = "com.pingpongx.business.account.biz.payout.PayoutTemplate.payoutRefund(PayoutTemplate.java:165)";

    @PostConstruct
    void init(){
        this.setType(SlsAlert.class.getTypeName());
        this.setAttr("content");
        this.setOperation(StringContains.getInstance());
        this.setExpected(except);
    }

}
