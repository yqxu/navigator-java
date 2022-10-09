package com.pingpongx.smb.warning.biz.rules.scene;

import com.pingpongx.smb.warning.biz.alert.CountConf;
import com.pingpongx.smb.warning.biz.alert.InhibitionFactory;
import com.pingpongx.smb.warning.biz.alert.ThresholdAlertConf;
import com.pingpongx.smb.warning.biz.alert.counter.CountContext;
import com.pingpongx.smb.warning.biz.alert.global.GlobalCountContext;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.DubboTimeOutInhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;
import com.pingpongx.smb.warning.biz.rules.Rule;
import com.pingpongx.smb.warning.biz.rules.RuleTrie;
import com.pingpongx.smb.warning.biz.rules.dubbo.timeout.StrExcept1;
import com.pingpongx.smb.warning.biz.rules.dubbo.timeout.StrExcept2;
import com.pingpongx.smb.warning.biz.rules.dubbo.timeout.StrExcept3;
import com.pingpongx.smb.warning.biz.rules.dubbo.timeout.StrExcept4;
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
    GlobalCountContext globalCountContext;

    @Autowired
    InhibitionFactory inhibitionFactory;
    Rule or;

    public static ThresholdAlertConf conf = new ThresholdAlertConf<>(5, TimeUnit.Minutes,10,10);

    @PostConstruct
    void init(){
        or = exp1.or(exp2).or(exp3).or(exp4);
        CountContext countContext = new CountContext(globalCountContext, conf);
        ruleTrie.put(or, countContext);
        Inhibition<ThirdPartAlert> inhibition = inhibitionFactory.getInhibition(conf);
        ruleTrie.put(or,inhibition);
    }

}
