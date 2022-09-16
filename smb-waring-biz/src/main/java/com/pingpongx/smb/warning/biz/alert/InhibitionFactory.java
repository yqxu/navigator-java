package com.pingpongx.smb.warning.biz.alert;

import com.pingpongx.smb.warning.biz.alert.counter.SlidingCounter;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.ThresholdInhibition;
import com.pingpongx.smb.warning.biz.moudle.dingding.FireResults;
import com.pingpongx.smb.warning.biz.rules.DubbleTimeOut;
import com.pingpongx.smb.warning.biz.rules.Rule;

public class InhibitionFactory {
    public static  <T> Inhibition<T> getInhibition(AlertConf<T> conf) {
        //目前就一种 写死，不走注册 路由
        conf.getAlertType();
        ThresholdInhibition in = new ThresholdInhibition<FireResults>();
        ThresholdAlertConf c = (ThresholdAlertConf)conf;
        in.setCounter(SlidingCounter.of(conf));
        //根据alertType 获取 rule
        DubbleTimeOut rule = new DubbleTimeOut();
        in.setMatcher(rule);
        in.setThreshold(c.getAlertThreshold());
        return in;
    }
}
