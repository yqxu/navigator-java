package com.pingpongx.smb.warning.biz.depends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class DingTalkClientFactory {
    @Autowired
    Map<String,PPDingTalkClient> dingTalkClientMapOrigin;

    Map<String,PPDingTalkClient> dingTalkClientMap;

    @PostConstruct
    private void init(){
        dingTalkClientMap = dingTalkClientMapOrigin.values().stream().collect(Collectors.toMap(client->client.getDepartName(),client->client));
    }

    public PPDingTalkClient instance(String departName){
        return dingTalkClientMap.get(departName);
    }
}
