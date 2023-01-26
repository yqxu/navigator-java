package com.pingpongx.smb.warning.biz.rules.dubbo.timeout;

import com.pingpongx.smb.export.module.ConfiguredLeafRule;
import com.pingpongx.smb.rule.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StrExcept1  extends ConfiguredLeafRule {
    private static String except="[DUBBO] Fail to connect to HeaderExchangeClient [channel=org.apache.dubbo.remoting.transport.netty4.NettyClient";

    @PostConstruct
    void init(){
        this.setType(SlsAlert.class.getName());
        this.setAttr("content");
        this.setOperation(StringContains.getInstance(SlsAlert.class.getSimpleName(),this.dependsAttr()));
        this.setExpected(except);
    }

}
