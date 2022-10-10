package com.pingpongx.smb.warning.biz.alert.routers.valref;

import io.vavr.Lazy;

public class ValRef {
    //TODO 可配置的数值提取器集合目前写死用反射
    String type;
    Lazy<String> val;

}
