package com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BatchMatcherFactory {
    @Autowired
    Map<String,BatchMatcher> originMatchers;
    Map<String,BatchMatcher> map = new ConcurrentHashMap<>();

    @PostConstruct
    void init(){
        originMatchers.values().stream().forEach(batchMatcher -> map.put((String) batchMatcher.supportedOperation().getIdentify(),batchMatcher));
    }

    public BatchMatcher getBatchMatcher(String identify){
        return map.get(identify);
    }

}
