package com.pingpongx.smb.rule.routers.operatiors.batch;


import com.pingpongx.smb.export.RuleConstant;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;


public class BatchMatcherFactory {

    public static Map<String, Supplier<BatchMatcher>> map = new ConcurrentHashMap<>();
    static {
        map.put(RuleConstant.Operations.StrEquals,BatchStrEquals::newInstance);
        map.put(RuleConstant.Operations.NumEquals,BatchStrEquals::newInstance);
        map.put(RuleConstant.Operations.StrContains,BatchStrContains::newInstance);
        map.put(RuleConstant.Operations.NumBiggerThen, BatchNumRange::newInstance);
    }

    public static BatchMatcher newBatchMatcher(String identify){
        return Optional.ofNullable(map.get(identify)).map(Supplier::get).orElse(null);
    }

}
