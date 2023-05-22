package com.pingpongx.smb.monitor.dal.entity.uiprops;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * 业务系统登录相关的参数配置化
 */
@Getter
@Setter
public class MerchantLoginParam {


    @Value("${uimonitor.merchant.host:https://ro-us.pingpongx.com}")
    private String monitorHost;

    @Value("${uimonitor.merchant.login.username:wangqq@pingpongx.com}")
    private String loginUserName;

    @Value("${uimonitor.merchant.login.password:qwe123}")
    private String loginPassword;

    @Value("${uimonitor.merchant.login.url:https://ro-us.pingpongx.com/entrance/signin}")
    private String loginUrl;

    @Value("${uimonitor.merchant.notify.phone:15105163710}")
    private List<String> phoneNumberList;

    @Value("${uimonitor.merchant.login.switch:false}")
    private String loginSwitch;
}
