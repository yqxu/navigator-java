package com.pingpongx.smb.warning.web.configuration;

import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.warning.biz.alert.model.GrafanaAlert;
import com.pingpongx.smb.warning.biz.alert.model.MerchantAlert;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuleEngineConfiguration {
    @Bean
    public Engine getEngine(){
        Engine engine = Engine.newInstance();
        engine.metadataCenter.reg(SlsAlert.class);
        engine.metadataCenter.reg(MerchantAlert.class);
        engine.metadataCenter.reg(GrafanaAlert.class);
        return engine;
    }
}
