package com.pingpongx.smb.monitor.biz.config;

import com.pingpongx.config.client.listener.ConfigListener;
import com.pingpongx.config.client.listener.ConfigListenerFactory;
import com.pingpongx.smb.monitor.biz.job.FMMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class UiMonitorConfigListener implements ConfigListener {

    @Autowired
    FMMonitor monitor;

    @PostConstruct
    void init(){
        ConfigListenerFactory.addListener("uimonitor.enable",this);
    }

    @Override
    public void onChange(String key, String value) {
        if(key.equals("uimonitor.enable")) {
            monitor.getHostParam().setEnable(value);
            log.info("uimonitor.enable config update:{}", value);
        }

    }
}
