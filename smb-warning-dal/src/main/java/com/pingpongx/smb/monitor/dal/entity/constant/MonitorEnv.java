package com.pingpongx.smb.monitor.dal.entity.constant;

public enum MonitorEnv {

    LOCAL("dev"),
    TEST("test"),
    PRE("pre"),
    PROD("prod"),
    ;

    String monitorEnv;
    MonitorEnv(String monitorEnv) {
        this.monitorEnv = monitorEnv;
    }


    public String getMonitorEnv() {
        return monitorEnv;
    }
}
