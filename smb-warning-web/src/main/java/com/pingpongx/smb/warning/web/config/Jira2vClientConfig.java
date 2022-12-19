package com.pingpongx.smb.warning.web.config;

import com.pingpongx.smb.cloud.client.Jira2vClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class Jira2vClientConfig {

    @Value("${customer.churn.jira2v.baseUrl:http://jira.pingpongx.com}")
    private String baseUrl;

    @Value("${customer.churn.jira2v.token:MTAwODgyMDk4MTY3OuZqSyEfQLeo/IFKtYhwSudgjme0}")
    private String token;

    @Value("${customer.churn.jira2v.issuetype:线上工单}")
    private String issuetype;

    @Value("${customer.churn.jira2v.project:RECALL}")
    private String project;

    @Value("${customer.churn.jira2v.summary:%s优先级客户%s，存在流失风险，请及时跟进}")
    private String summary;

    @Value("${customer.churn.jira2v.desc:{color:#FF0000}请及时跟进存在流失风险的重点客户，并认真填写工单信息；P0级别客户需要任务生成1周后完成；P1级别客户需要任务生成2周后完成！！！{color}}")
    private String desc;

    @Bean
    public Jira2vClient jira2vClient() {
        return new Jira2vClient(baseUrl, token);
    }

}
