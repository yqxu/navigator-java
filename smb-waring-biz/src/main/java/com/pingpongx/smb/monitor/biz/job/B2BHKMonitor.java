package com.pingpongx.smb.monitor.biz.job;

import com.alibaba.fastjson.JSON;
import com.pingpongx.job.core.handler.annotation.JobHandler;
import com.pingpongx.smb.monitor.biz.pages.b2b.LoginPage;
import com.pingpongx.smb.monitor.dal.entity.uiprops.B2BHKLoginParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component
@JobHandler("B2BHKMonitor")
public class B2BHKMonitor extends MonitorTemplateJob {

    @Resource
    private B2BHKLoginParam b2bHKLoginParam;

    @Override
    public void initEnv() {
        super.setHost(b2bHKLoginParam.getMonitorB2BHKHost());
        super.setDingGroup("B2BHK");
        super.setBusiness(this.getClass().getSimpleName());
        super.setPhoneNumber(b2bHKLoginParam.getPhoneNumber());
        super.setLoginSwitch(b2bHKLoginParam.getLoginSwitch());
    }

    @Override
    public void login() {
        log.info("B2BHKMonitor 参数信息：{}", JSON.toJSONString(b2bHKLoginParam));
        LoginPage loginPage = new LoginPage();
        loginPage.setPage(super.getPage());
        loginPage.setLoginUrl(b2bHKLoginParam.getB2bHKLoginUrl());
        loginPage.setLoginUsername(b2bHKLoginParam.getB2bHKLoginUserName());
        loginPage.setLoginPassword(b2bHKLoginParam.getB2bHKLoginPassword());

        loginPage.login();
    }

    @Override
    public void actions() {

    }

    @Override
    public void logout() {

    }
}
