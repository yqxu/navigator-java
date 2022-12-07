package com.pingpongx.smb.warning.biz.rules.scene.temp;

import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.Rule;
import com.pingpongx.smb.warning.biz.alert.InhibitionFactory;
import com.pingpongx.smb.warning.biz.alert.ThresholdAlertConf;
import com.pingpongx.smb.warning.biz.alert.counter.CountContext;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;
import com.pingpongx.smb.warning.biz.rules.temp.TempRule1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TempTransactionExp {
    @Autowired
    TempRule1 exp;
    @Autowired
    Engine engine;

    @Autowired
    InhibitionFactory inhibitionFactory;
    Rule or;

    public static ThresholdAlertConf conf = new ThresholdAlertConf(5, TimeUnit.Minutes,10,10);

    public static  String sceneId = "TempTransactionExp";
    @PostConstruct
    void init(){
        or = exp;
        CountContext countContext = new CountContext( sceneId,conf);
        engine.put(or, countContext);
        Inhibition<ThirdPartAlert> inhibition = inhibitionFactory.newInhibition(sceneId,conf);
        engine.put(or,inhibition);
    }

}
