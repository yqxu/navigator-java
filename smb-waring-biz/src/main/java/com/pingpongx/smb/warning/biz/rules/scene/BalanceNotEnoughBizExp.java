package com.pingpongx.smb.warning.biz.rules.scene;

import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.Rule;
import com.pingpongx.smb.warning.biz.alert.InhibitionFactory;
import com.pingpongx.smb.warning.biz.alert.ThresholdAlertConf;
import com.pingpongx.smb.warning.biz.alert.counter.CountContext;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;
import com.pingpongx.smb.warning.biz.rules.bizexp.BizExpRule2;
import com.pingpongx.smb.warning.biz.rules.scene.configure.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BalanceNotEnoughBizExp {
    @Autowired
    Engine engine;
    @Autowired
    BizExpRule2 exp2;
//    @Autowired
//    RuleTrie ruleTrie;

    @Autowired
    InhibitionFactory inhibitionFactory;
    Rule or;

    public static ThresholdAlertConf conf = new ThresholdAlertConf(5, TimeUnit.Minutes,10,10);

    public static Scene scene = new Scene();
    @PostConstruct
    void init(){
        or = exp2;
        scene.setIdentity("BalanceNotEnough");
        scene.setCountWith(conf);
        CountContext countContext = new CountContext(scene);
        engine.put(or, countContext);
        Inhibition<ThirdPartAlert> inhibition = inhibitionFactory.newInhibition(scene.getIdentity(),conf);
        engine.put(or,inhibition);
    }

}
