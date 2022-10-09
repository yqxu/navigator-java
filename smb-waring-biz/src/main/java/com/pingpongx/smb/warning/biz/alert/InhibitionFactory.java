package com.pingpongx.smb.warning.biz.alert;

import com.pingpongx.smb.warning.biz.alert.counter.SlidingCounter;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.DubboTimeOutInhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.SimpleCounterThresholdInhibition;
import com.pingpongx.smb.warning.biz.moudle.dingding.FireResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/***
 * 预留 当抑制策略较多时装配使用，支持配置化时，从配置项生成等
 */
@Component
public class InhibitionFactory {
    @Autowired
    ApplicationContext applicationContext;

    public  <T> Inhibition<T> getInhibition(AlertConf<T> conf) {
        //目前就一种 写死，不走注册 路由
//        conf.getAlertType();
        DubboTimeOutInhibition in = new DubboTimeOutInhibition();
        ThresholdAlertConf c = (ThresholdAlertConf)conf;
        in.setThreshold(c.getAlertThreshold());
        in.setApplicationContext(applicationContext);
        return in;
    }
}
