package com.pingpongx.smb.warning.biz.rules.scene;

import com.pingpongx.smb.warning.biz.alert.CountConf;
import com.pingpongx.smb.warning.biz.alert.InhibitionFactory;
import com.pingpongx.smb.warning.biz.alert.ThresholdAlertConf;
import com.pingpongx.smb.warning.biz.alert.counter.CountContext;
import com.pingpongx.smb.warning.biz.alert.global.GlobalCountContext;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;
import com.pingpongx.smb.warning.biz.rules.Rule;
import com.pingpongx.smb.warning.biz.rules.RuleTrie;
import com.pingpongx.smb.warning.biz.rules.dubbo.timeout.*;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DubbleTimeOut {
    @Autowired
    StrExcept1 exp1;
    @Autowired
    StrExcept2 exp2;
    @Autowired
    StrExcept3 exp3;
    @Autowired
    StrExcept4 exp4;
    @Autowired
    RuleTrie ruleTrie;

    @Autowired
    InhibitionFactory inhibitionFactory;
    Rule normalRule;

    Rule gateWayRule;

    public static ThresholdAlertConf conf = new ThresholdAlertConf(5, TimeUnit.Minutes,10,10);
    public static ThresholdAlertConf gateWayConf = new ThresholdAlertConf(5, TimeUnit.Minutes,10,200);

    @PostConstruct
    void init(){
        AppName appName = new AppName();
        AppName appNameNot = new AppName();
        appNameNot.init();
        appName.init();
        appNameNot.setNot(true);

        gateWayRule = exp1.or(exp2).or(exp3).or(exp4).and(appName);
        normalRule = exp1.or(exp2).or(exp3).or(exp4).and(appNameNot);

        CountContext countContext = new CountContext( conf);
        ruleTrie.put(normalRule,countContext);
        Inhibition<ThirdPartAlert> inhibition = inhibitionFactory.getInhibition(conf);
        ruleTrie.put(normalRule,inhibition);

        CountContext gateWayContext = new CountContext( gateWayConf);
        ruleTrie.put(gateWayRule,gateWayContext);
        Inhibition<ThirdPartAlert> getWayInhibition = inhibitionFactory.getInhibition(gateWayConf);
        ruleTrie.put(gateWayRule,getWayInhibition);
    }

}
