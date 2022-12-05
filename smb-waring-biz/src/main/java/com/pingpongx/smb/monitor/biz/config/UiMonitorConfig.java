package com.pingpongx.smb.monitor.biz.config;

import com.pingpongx.smb.monitor.dal.entity.uiprops.HostParam;
import com.pingpongx.smb.monitor.dal.entity.uiprops.LoginParam;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UiMonitorConfig {

    @Bean
    public LoginParam loginParam() {
        return new LoginParam();
    }

    @Bean
    public HostParam hostParam() {
        return new HostParam();
    }
}
