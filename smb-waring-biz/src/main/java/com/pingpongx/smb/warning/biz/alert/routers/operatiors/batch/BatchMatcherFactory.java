package com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch;

import com.pingpongx.smb.warning.biz.alert.routers.operatiors.StrEquals;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;


public class BatchMatcherFactory {

    public static Map<String, Supplier<BatchMatcher>> map = new ConcurrentHashMap<>();
    static {
        map.put(Constant.Operations.Equals,BatchStrEquals::newInstance);
        map.put(Constant.Operations.StrContains,BatchStrContains::newInstance);
    }

    public static BatchMatcher newBatchMatcher(String identify){
        return Optional.ofNullable(map.get(identify)).map(Supplier::get).orElse(null);
    }

}
