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
public class FMLoginParam {


    @Value("${uimonitor.fm.host:https://flowmore.pingpongx.com}")
    private String monitorHost;

    @Value("${uimonitor.fm.login.username:fuww@pingpongx.com}")
    private String loginUserName;

    @Value("${uimonitor.fm.login.password:pingpong0000}")
    private String loginPassword;

    @Value("${uimonitor.fm.login.url:https://flowmore.pingpongx.com/entrance/signin}")
    private String loginUrl;

    @Value("${uimonitor.fm.notify.phone:15105163710}")
    private List<String> phoneNumberList;

    @Value("${uimonitor.fm.login.switch:false}")
    private String loginSwitch;

}
