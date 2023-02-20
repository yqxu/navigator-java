package com.pingpongx.smb.monitor.dal.entity.uiprops;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

/**
 * 业务系统登录相关的参数配置化
 */
@Getter
@Setter
public class MerchantLoginParam {


    @Value("${uimonitor.merchant.host:https://us.pingpongx.com}")
    private String monitorMerchantHost;

    @Value("${uimonitor.merchant.login.username:wangqq@pingpongx.com}")
    private String merchantLoginUserName;

    @Value("${uimonitor.merchant.login.password:qwe123}")
    private String merchantLoginPassword;

    @Value("${uimonitor.merchant.login.url:https://ro-us.pingpongx.com/entrance/signin}")
    private String merchantLoginUrl;

}
