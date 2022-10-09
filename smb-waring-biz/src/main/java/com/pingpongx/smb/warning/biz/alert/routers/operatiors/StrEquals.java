package com.pingpongx.smb.warning.biz.alert.routers.operatiors;

import com.pingpongx.smb.warning.biz.constant.Constant;

public class StrEquals implements MatchOperation<String> {

    public static StrEquals getInstance(){
        return new StrEquals();
    }

    @Override
    public String getIdentify() {
        return Constant.Operations.Equals;
    }

    @Override
    public int sortBy() {
        return 0;
    }
}
