package com.pingpongx.smb.warning.biz.alert;

import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.SimpleCounterThresholdInhibition;
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

    public  <T> Inhibition<T> getInhibition(AlertConf conf) {
        SimpleCounterThresholdInhibition in = new SimpleCounterThresholdInhibition<>();
        ThresholdAlertConf c = (ThresholdAlertConf)conf;
        in.setThreshold(c.getAlertThreshold());
        in.setApplicationContext(applicationContext);
        return in;
    }
}
