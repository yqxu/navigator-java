package com.pingpongx.smb.warning.biz.rules.scene.b2bgateway;

import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.warning.biz.alert.InhibitionFactory;
import com.pingpongx.smb.warning.biz.alert.ThresholdAlertConf;
import com.pingpongx.smb.warning.biz.alert.counter.CountContext;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class GateWayIgnore {
    @Autowired
    Engine engine;
    @Autowired
    BusinessGateway exp1;

    @Autowired
    InhibitionFactory inhibitionFactory;

    public static ThresholdAlertConf gateWayConf = new ThresholdAlertConf(5, TimeUnit.Minutes,10,200);

    public static String gateWayIgnore = "GateWayIgnore";
    @PostConstruct
    void init(){
        CountContext countContext = new CountContext(gateWayIgnore, gateWayConf);
        engine.put(exp1,countContext);
        Inhibition<ThirdPartAlert> inhibition = inhibitionFactory.newInhibition(gateWayIgnore,gateWayConf);
        engine.put(exp1,inhibition);
    }

}
