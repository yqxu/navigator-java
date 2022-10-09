package com.pingpongx.smb.warning.biz.alert.model;

import com.pingpongx.smb.warning.biz.moudle.IdentityPath;

import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ThirdPartAlert extends Serializable,RouteAble,CountAble{
    String throwAppName();

    String throwContent();
}
