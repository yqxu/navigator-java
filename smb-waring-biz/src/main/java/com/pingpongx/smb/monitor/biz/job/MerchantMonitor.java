package com.pingpongx.smb.monitor.biz.job;

import com.alibaba.fastjson.JSON;
import com.microsoft.playwright.Page;
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
        super.setHost(merchantLoginParam.getMonitorMerchantHost());
        super.setDingGroup("MERCHANT");
        super.setBusiness(this.getClass().getSimpleName());
    }

    @Override
    public void actions(Page page) {
        log.info("MerchantMonitor 参数信息：{}", JSON.toJSONString(merchantLoginParam));

        LoginPage loginPage = new LoginPage();
        loginPage.setPage(page);
        loginPage.setLoginUrl(merchantLoginParam.getMerchantLoginUrl());
        loginPage.setLoginUsername(merchantLoginParam.getMerchantLoginUserName());
        loginPage.setLoginPassword(merchantLoginParam.getMerchantLoginPassword());

        loginPage.login();

        HomePage homePage = new HomePage();
        homePage.setPage(page);
        homePage.pageSearch();

        loginPage.logout();

    }

}
