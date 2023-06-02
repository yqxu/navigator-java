package com.pingpongx.smb.monitor.biz.job;

import com.alibaba.fastjson.JSON;
import com.pingpongx.job.core.handler.annotation.JobHandler;
import com.pingpongx.smb.monitor.biz.pages.merchant.HomePage;
import com.pingpongx.smb.monitor.biz.pages.merchant.LoginPage;
import com.pingpongx.smb.monitor.dal.entity.uiprops.MerchantLoginParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@JobHandler
public class MerchantMonitor extends MonitorTemplateJob {

    @Resource
    private MerchantLoginParam merchantLoginParam;

    @Override
    public void initEnv() {
        super.setHost(merchantLoginParam.getMonitorHost());
        super.setBusiness("merchant");
        super.setPhoneNumberList(merchantLoginParam.getPhoneNumberList());
        super.setLoginSwitch(merchantLoginParam.getLoginSwitch());
    }

    @Override
    public void login() {
        log.info("MerchantMonitor 参数信息：{}", JSON.toJSONString(merchantLoginParam));

        LoginPage loginPage = new LoginPage();
        loginPage.setPage(super.getPage());
        loginPage.setLoginUrl(merchantLoginParam.getLoginUrl());
        loginPage.setLoginUsername(merchantLoginParam.getLoginUserName());
        loginPage.setLoginPassword(merchantLoginParam.getLoginPassword());

        loginPage.login();
    }

    @Override
    public void actions() {
        HomePage homePage = new HomePage();
        homePage.setPage(super.getPage());
        homePage.pageSearch();
    }

    @Override
    public void logout() {
        LoginPage loginPage = new LoginPage();
        loginPage.setPage(super.getPage());
        loginPage.logout();
    }

}
