package com.pingpongx.smb.warning.biz.rules.scene;

import com.pingpongx.smb.warning.biz.alert.InhibitionFactory;
import com.pingpongx.smb.warning.biz.alert.ThresholdAlertConf;
import com.pingpongx.smb.warning.biz.alert.counter.CountContext;
import com.pingpongx.smb.warning.biz.alert.global.GlobalCountContext;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;
import com.pingpongx.smb.warning.biz.rules.Rule;
import com.pingpongx.smb.warning.biz.rules.RuleTrie;
import com.pingpongx.smb.warning.biz.rules.bizexp.BizExpRule;
import com.pingpongx.smb.warning.biz.rules.dubbo.timeout.StrExcept1;
import com.pingpongx.smb.warning.biz.rules.dubbo.timeout.StrExcept2;
import com.pingpongx.smb.warning.biz.rules.dubbo.timeout.StrExcept3;
import com.pingpongx.smb.warning.biz.rules.dubbo.timeout.StrExcept4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BizExp {
    @Autowired
    BizExpRule exp1;
    @Autowired
    RuleTrie ruleTrie;

    @Autowired
    InhibitionFactory inhibitionFactory;
    Rule or;

    public static ThresholdAlertConf conf = new ThresholdAlertConf<>(5, TimeUnit.Minutes,10,10);

    @PostConstruct
    void init(){
        or = exp1;
        CountContext countContext = new CountContext( conf);
        ruleTrie.put(or, countContext);
        Inhibition<ThirdPartAlert> inhibition = inhibitionFactory.getInhibition(conf);
        ruleTrie.put(or,inhibition);
    }

}
