package com.pingpongx.smb.warning.biz.rules.bizexp;

import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.rules.ConfiguredLeafRule;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BizExpRule2 extends ConfiguredLeafRule {
    private static String except = "request calculate failed:资金不足";

    @PostConstruct
    void init(){
        this.setType(SlsAlert.class.getSimpleName());
        this.setAttr("content");
        this.setOperation(StringContains.getInstance());
        this.setExcepted(except);
    }
}
