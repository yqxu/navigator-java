package com.pingpongx.smb.warning.web.config;

import com.pingpongx.smb.cloud.dingtalk.client.DingtalkClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class DingtalkClientConfig {

    @Value("${customer.churn.dingtalk.baseUrl:https://api.dingtalk.com}")
    private String baseUrl;

    @Value("${customer.churn.dingtalk.oapiUrl:https://oapi.dingtalk.com}")
    private String oapiUrl;

    @Value("${customer.churn.dingtalk.appId}")
    private String appId;

    @Value("${customer.churn.dingtalk.appSecret}")
    private String appSecret;

    @Bean
    public DingtalkClient dingtalkClient() {
        return new DingtalkClient(baseUrl, oapiUrl, appId, appSecret);
    }

}
