package com.pingpongx.smb.monitor.dal.entity.constant;

public enum BusinessLine {

    Merchant("主站"),
    FM("福贸");

    String businessLine;
    BusinessLine(String businessLine) {
        this.businessLine = businessLine;
    }


    public String getBusinessLine() {
        return businessLine;
    }

}
