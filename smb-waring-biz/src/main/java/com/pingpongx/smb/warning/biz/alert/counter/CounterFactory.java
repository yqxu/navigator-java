package com.pingpongx.smb.warning.biz.alert.counter;

import com.pingpongx.smb.warning.biz.alert.AlertConf;
import com.pingpongx.smb.warning.biz.alert.CountConf;
import org.springframework.stereotype.Component;

public class CounterFactory {
    public static Counter getCounter(CountConf conf){
        SlidingCounter slidingCounter = SlidingCounter.of(conf);
        return slidingCounter;
    }
}
