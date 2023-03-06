package com.pingpongx.smb.monitor.biz.config;

import com.microsoft.playwright.Playwright;
import com.pingpongx.smb.monitor.dal.entity.uiprops.FMLoginParam;
import com.pingpongx.smb.monitor.dal.entity.uiprops.MerchantLoginParam;
import com.pingpongx.smb.monitor.dal.entity.uiprops.MonitorEnvParam;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UiMonitorConfig {

    @Bean
    public FMLoginParam fmLoginParam() {
        return new FMLoginParam();
    }

    @Bean
    public MonitorEnvParam monitorEnvParam() {
        return new MonitorEnvParam();
    }

    @Bean
    public MerchantLoginParam merchantLoginParam() {
        return new MerchantLoginParam();
    }

}
