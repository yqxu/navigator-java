package com.pingpongx.smb.warning.biz.alert.model;

import java.io.Serializable;

public interface ThirdPartAlert extends Serializable,RouteAble,CountAble{
    String throwAppName();

    String throwContent();

    String depart();
    void departSet(String depart);
}
