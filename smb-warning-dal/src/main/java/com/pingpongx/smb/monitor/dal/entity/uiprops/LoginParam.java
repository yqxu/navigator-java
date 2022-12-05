package com.pingpongx.smb.monitor.dal.entity.uiprops;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

/**
 * 业务系统登录相关的参数配置化
 */
@Getter
@Setter
public class LoginParam {


    @Value("${uimonitor.fm.host}")
    private String monitorFmHost;

    @Value("${uimonitor.fm.login.username}")
    private String fmLoginUserName;

    @Value("${uimonitor.fm.login.password}")
    private String fmLoginPassword;

    @Value("${uimonitor.fm.login.url}")
    private String fmLoginUrl;

}
