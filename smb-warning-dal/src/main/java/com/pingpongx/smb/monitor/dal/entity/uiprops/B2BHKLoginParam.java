package com.pingpongx.smb.monitor.dal.entity.uiprops;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Getter
@Setter
public class B2BHKLoginParam {

    @Value("${uimonitor.b2b.host:https://test2-business.pingpongx.com}")
    private String monitorHost;

    @Value("${uimonitor.b2b.login.url:https://test2-business.pingpongx.com/entrance/signin}")
    private String loginUrl;

    @Value("${uimonitor.b2b.hk.login.username:skyler_xub2b@163.com}")
    private String loginUserName;

    @Value("${uimonitor.b2b.hk.login.password:pingpong0000}")
    private String loginPassword;

    @Value("${uimonitor.b2b.notify.phone:15105163710}")
    private List<String> phoneNumberList;

    @Value("${uimonitor.b2b.login.switch:false}")
    private String loginSwitch;
}
