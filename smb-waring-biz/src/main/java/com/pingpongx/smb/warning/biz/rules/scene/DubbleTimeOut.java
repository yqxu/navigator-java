package com.pingpongx.smb.warning.biz.rules.scene;

import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.Rule;
import com.pingpongx.smb.warning.biz.alert.InhibitionFactory;
import com.pingpongx.smb.warning.biz.alert.ThresholdAlertConf;
import com.pingpongx.smb.warning.biz.alert.counter.CountContext;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;
import com.pingpongx.smb.warning.biz.rules.dubbo.timeout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DubbleTimeOut {
    @Autowired
    Engine engine;
    @Autowired
    StrExcept1 exp1;
    @Autowired
    StrExcept2 exp2;
    @Autowired
    StrExcept3 exp3;
    @Autowired
    StrExcept4 exp4;


    @Autowired
    InhibitionFactory inhibitionFactory;
    Rule normalRule;

    Rule gateWayRule;

    public static ThresholdAlertConf conf = new ThresholdAlertConf(5, TimeUnit.Minutes,10,10);
    public static ThresholdAlertConf gateWayConf = new ThresholdAlertConf(5, TimeUnit.Minutes,10,200);

    public static String dubboTimeOutScene = "DubboTimeOutNormal";
    public static String dubboTimeOutGateWayScene = "DubboTimeOutGateWay";
    @PostConstruct
    void init(){
        AppName appName = new AppName();
        AppName appNameNot = new AppName();
        appNameNot.init();
        appName.init();
        appNameNot.setNot(true);

        gateWayRule = exp1.or(exp2).or(exp3).or(exp4).and(appName);
        normalRule = exp1.or(exp2).or(exp3).or(exp4).and(appNameNot);

        CountContext countContext = new CountContext(dubboTimeOutScene, conf);
        engine.put(normalRule,countContext);
        Inhibition<ThirdPartAlert> inhibition = inhibitionFactory.newInhibition(dubboTimeOutScene,conf);
        engine.put(normalRule,inhibition);

        CountContext gateWayContext = new CountContext(dubboTimeOutGateWayScene, gateWayConf);
        engine.put(gateWayRule,gateWayContext);
        Inhibition<ThirdPartAlert> getWayInhibition = inhibitionFactory.newInhibition(dubboTimeOutGateWayScene,gateWayConf);
        engine.put(gateWayRule,getWayInhibition);
    }

}
