package com.pingpongx.smb.warning.biz.alert.routers.operatiors;

import com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch.BatchMatcher;
import com.pingpongx.smb.warning.biz.constant.Constant;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;


public class OperationFactory {
    public static Map<String, Supplier<MatchOperation>> factories = new ConcurrentHashMap<>();
    static {
        factories.put(Constant.Operations.Equals,()->StrEquals.getInstance());
        factories.put(Constant.Operations.StrContains,()->StringContains.getInstance());
    }
    public static MatchOperation getInstance(String name){
        return Optional.ofNullable(factories.get(name)).map(Supplier::get).orElse(null);
    }
}
