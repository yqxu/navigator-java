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

    Map<String,PPDingTalkClient> departMap;
    Map<String,PPDingTalkClient> appNameMap;

    @PostConstruct
    private void init(){
        departMap = dingTalkClientMapOrigin.values().stream().collect(Collectors.toMap(client->client.getDepartName(),client->client));
        departMap = dingTalkClientMapOrigin.values().stream().collect(Collectors.toMap(client->client.getDepartName(),client->client));
    }

    public PPDingTalkClient getByDepart(String departName){
        return departMap.get(departName);
    }

    public PPDingTalkClient getByAppName(String appName){
        return appNameMap.get(appName);
    }
}
