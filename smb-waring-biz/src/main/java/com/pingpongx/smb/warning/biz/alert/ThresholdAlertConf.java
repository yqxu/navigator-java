package com.pingpongx.smb.warning.biz.alert;

import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;

import java.io.Serializable;


public class ThresholdAlertConf implements AlertConf, CountConf, Serializable {
    public ThresholdAlertConf(long duration, TimeUnit unit, int partNum, long alertThreshold) {
        this.duration = duration;
        this.unit = unit;
        this.partNum = partNum;
        this.alertThreshold = alertThreshold;
    }

    long duration;
    TimeUnit unit;
    int partNum;
    long alertThreshold;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public int getPartNum() {
        return partNum;
    }

    public void setPartNum(int partNum) {
        this.partNum = partNum;
    }

    public long getAlertThreshold() {
        return alertThreshold;
    }

    public void setAlertThreshold(long alertThreshold) {
        this.alertThreshold = alertThreshold;
    }



}
