package com.pingpongx.smb.warning.biz.rules.bizexp;

import com.pingpongx.smb.rule.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.rules.ConfiguredLeafRule;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BizExpRule extends ConfiguredLeafRule {
    private static String except = "com.pingpongx.business.common.exception.BizException: 邮箱已被注册！";
    @PostConstruct
    void init(){
        this.setType(SlsAlert.class);
        this.setAttr("content");
        this.setOperation(StringContains.getInstance(SlsAlert.class.getSimpleName(),this.dependsAttr()));
        this.setExpected(except);
    }

}
