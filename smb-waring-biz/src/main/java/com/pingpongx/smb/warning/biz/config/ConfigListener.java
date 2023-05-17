package com.pingpongx.smb.warning.biz.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pingpongx.config.client.listener.ConfigListenerFactory;
import com.pingpongx.smb.warning.biz.depends.ConfiguredDingTalkClient;
import com.pingpongx.smb.warning.biz.depends.DingTalkClientFactory;
import com.pingpongx.smb.warning.biz.depends.DingtalkClientRouter;
import com.pingpongx.smb.warning.biz.rules.scene.ConfiguredScene;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class ConfigListener implements com.pingpongx.config.client.listener.ConfigListener {

    @Autowired
    ConfiguredScene inhibitionScenes;

    @Autowired
    DingTalkClientFactory factory;
    @PostConstruct
    void init(){
        ConfigListenerFactory.addListener("configured.scene",this);
        ConfigListenerFactory.addListener("dingtalk.notify.urls",this);
    }

    @Override
    public void onChange(String key, String value) {
        if(key.equals("configured.scene")){
            inhibitionScenes.loadConfigStr(value);
            log.info("Inhibition scene add by config upgrade:\n"+value);
        }
        if (key.equals("dingtalk.notify.urls")){
            JSONObject json = JSON.parseObject(value);
            json.entrySet().stream().forEach(stringObjectEntry -> {
                ConfiguredDingTalkClient client = new ConfiguredDingTalkClient(stringObjectEntry.getValue().toString());
                client.setDepart(stringObjectEntry.getKey());
                factory.put(client);
            });
        }
    }
}
