package com.pingpongx.smb.warning.web.config;

import com.pingpongx.flowmore.cloud.base.commom.constants.BaseConstants;
import com.pingpongx.smb.open.sdk.core.client.DefaultSMBClient;
import com.pingpongx.smb.open.sdk.core.client.SMBClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class SMBClientConfig {

    @Value("${flowmore-base.base.oauth.domain}")
    private String smbBaseUrl;

    @Bean
    public SMBClient smbClient() {
        return new DefaultSMBClient(smbBaseUrl, System.getProperty(BaseConstants.AUTH_APP_ID), System.getProperty(BaseConstants.AUTH_APP_SECRET));
    }
}
