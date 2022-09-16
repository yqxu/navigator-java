package com.pingpongx.smb.warning.biz.alert;

import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;

public interface CountNeededAlertConf<T> extends AlertConf<T> {

    long getDuration();
    TimeUnit getUnit();
    int getPartNum();

}
