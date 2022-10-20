package com.pingpongx.smb.warning.biz.alert.routers.operatiors;

import com.pingpongx.smb.warning.biz.constant.Constant;


public class StringContains implements MatchOperation<String> {
    public static StringContains getInstance(){
        return new StringContains();
    }

    @Override
    public String getIdentify() {
        return Constant.Operations.StrContains;
    }

    @Override
    public int sortBy() {
        return 100;
    }
}
