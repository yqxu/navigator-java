package com.pingpongx.smb.warning.biz.rules.scene.merchant;

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
public class GatewayIgnoreScene {
    @Autowired
    Engine engine;
    @Autowired
    MerchantGatewayWebIgnore exp1;

    @Autowired
    InhibitionFactory inhibitionFactory;

    public static ThresholdAlertConf conf = new ThresholdAlertConf(5, TimeUnit.Minutes,10,20);

    public static String warnIgnore = "GateWayWebIgnore";
    @PostConstruct
    void init(){
        CountContext countContext = new CountContext(warnIgnore, conf);
        engine.put(exp1,countContext);
        Inhibition<ThirdPartAlert> inhibition = inhibitionFactory.newInhibition(warnIgnore, conf);
        engine.put(exp1,inhibition);
    }

}
