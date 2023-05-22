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
        super.setHost(b2bHKLoginParam.getMonitorHost());
        super.setDingGroup("B2B");
        super.setBusiness(this.getClass().getSimpleName());
        super.setPhoneNumber(b2bHKLoginParam.getPhoneNumber());
        super.setLoginSwitch(b2bHKLoginParam.getLoginSwitch());
    }

    @Override
    public void login() {
        log.info("B2BHKMonitor 参数信息：{}", JSON.toJSONString(b2bHKLoginParam));
        LoginPage loginPage = new LoginPage();
        loginPage.setPage(super.getPage());
        loginPage.setLoginUrl(b2bHKLoginParam.getLoginUrl());
        loginPage.setLoginUsername(b2bHKLoginParam.getLoginUserName());
        loginPage.setLoginPassword(b2bHKLoginParam.getLoginPassword());

        loginPage.login();
    }

    @Override
    public void actions() {

    }

    @Override
    public void logout() {

    }
}
