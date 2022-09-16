package com.pingpongx.smb.warning.biz.alert.threshold;

public enum TimeUnit {
    Seconds(1000L),
    Minutes(60* Seconds.millisecond),
    Hours(60* Minutes.millisecond),
    Days(24* Hours.millisecond);
    long millisecond;
    TimeUnit(Long m){
        this.millisecond = m;
    }

    public long getMillisecond() {
        return millisecond;
    }
}
