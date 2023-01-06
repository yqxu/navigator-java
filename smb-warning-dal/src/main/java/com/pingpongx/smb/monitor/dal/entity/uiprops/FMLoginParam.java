package com.pingpongx.smb.monitor.dal.entity.uiprops;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

/**
 * 业务系统登录相关的参数配置化
 */
@Getter
@Setter
public class FMLoginParam {


    @Value("${uimonitor.fm.host:https://flowmore.pingpongx.com}")
    private String monitorFmHost;

    @Value("${uimonitor.fm.login.username:fuww@pingpongx.com}")
    private String fmLoginUserName;

    @Value("${uimonitor.fm.login.password:pingpong0000}")
    private String fmLoginPassword;

    @Value("${uimonitor.fm.login.url:https://flowmore.pingpongx.com/entrance/signin}")
    private String fmLoginUrl;

}
