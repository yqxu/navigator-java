package com.pingpongx.smb.warning.biz.alert;

import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;

public interface CountConf  {

    long getDuration();
    TimeUnit getUnit();
    int getPartNum();

}
