package com.pingpongx.smb.monitor.dal.entity.uiprops;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class B2BHKLoginParam {

    @Value("${uimonitor.b2b.host:https://test2-business.pingpongx.com}")
    private String monitorB2BHKHost;

    @Value("${uimonitor.b2b.login.username:skyler_xub2b@163.com}")
    private String b2bHKLoginUserName;

    @Value("${uimonitor.b2b.login.password:pingpong0000}")
    private String b2bHKLoginPassword;

    @Value("${uimonitor.b2b.login.url:https://test2-business.pingpongx.com/entrance/signin}")
    private String b2bHKLoginUrl;

    @Value("${uimonitor.notify.phone:15105163710}")
    private String phoneNumber;
}
