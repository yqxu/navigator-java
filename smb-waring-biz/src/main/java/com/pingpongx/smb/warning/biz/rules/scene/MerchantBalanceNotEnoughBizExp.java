package com.pingpongx.smb.warning.biz.rules.scene;

import com.pingpongx.smb.warning.biz.alert.InhibitionFactory;
import com.pingpongx.smb.warning.biz.alert.ThresholdAlertConf;
import com.pingpongx.smb.warning.biz.alert.counter.CountContext;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;
import com.pingpongx.smb.warning.biz.rules.Rule;
import com.pingpongx.smb.warning.biz.rules.RuleTrie;
import com.pingpongx.smb.warning.biz.rules.bizexp.BizExpRule2;
import com.pingpongx.smb.warning.biz.rules.bizexp.BizExpRule3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MerchantBalanceNotEnoughBizExp {

    @Autowired
    BizExpRule3 exp3;
    @Autowired
    RuleTrie ruleTrie;

    @Autowired
    InhibitionFactory inhibitionFactory;
    Rule or;

    public static ThresholdAlertConf conf = new ThresholdAlertConf<>(5, TimeUnit.Minutes,10,10);

    @PostConstruct
    void init(){
        or = exp3;
        CountContext countContext = new CountContext( conf);
        ruleTrie.put(or, countContext);
        Inhibition<ThirdPartAlert> inhibition = inhibitionFactory.getInhibition(conf);
        ruleTrie.put(or,inhibition);
    }

}
