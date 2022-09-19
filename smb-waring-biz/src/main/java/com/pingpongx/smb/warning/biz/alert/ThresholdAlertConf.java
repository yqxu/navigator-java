package com.pingpongx.smb.warning.biz.alert;

import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;



public class ThresholdAlertConf<T> implements AlertConf<T>,CountNeededAlertConf<T>{
    public ThresholdAlertConf(long duration, TimeUnit unit, int partNum, long alertThreshold, Class<T> sourceType) {
        this.duration = duration;
        this.unit = unit;
        this.partNum = partNum;
        this.alertThreshold = alertThreshold;
        this.sourceType = sourceType;
    }

    long duration;
    TimeUnit unit;
    int partNum;
    long alertThreshold;
    Class<T> sourceType;

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

    @Override
    public String getAlertType() {
        return "Threshold";
    }

    @Override
    public Class<T> sourceType() {
        return sourceType;
    }

}
