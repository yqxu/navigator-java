package com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Component
public class BatchMatcherFactory {
    @Autowired
    Map<String,BatchMatcher> originMatchers;
    Map<String, Supplier<BatchMatcher>> map = new ConcurrentHashMap<>();

    @PostConstruct
    void init(){
        originMatchers.values().stream().forEach(batchMatcher -> map.put((String) batchMatcher.supportedOperation().getIdentify(),()->batchMatcher.newInstance()));
    }

    public BatchMatcher newBatchMatcher(String identify){
        return map.get(identify).get();
    }

}
