//package com.pingpongx.smb.monitor.biz.job;
//
//import com.microsoft.playwright.Page;
//import com.pingpongx.smb.monitor.biz.pages.merchant.HomePage;
//import com.pingpongx.smb.monitor.biz.pages.merchant.LoginPage;
//import com.pingpongx.smb.monitor.dal.entity.uiprops.MerchantLoginParam;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@EnableScheduling
//public class MerchantMonitor extends MonitorTemplate {
//
//    @Autowired
//    private MerchantLoginParam merchantLoginParam;
//
//    private int index;
//
//    @Override
//    public void actions(Page page) {
//        super.setHost(merchantLoginParam.getMonitorMerchantHost());
//        super.setDingGroup("MERCHANT");
//
//        LoginPage loginPage = new LoginPage();
//        loginPage.setPage(page);
//        loginPage.setLoginUrl(merchantLoginParam.getMerchantLoginUrl());
//        loginPage.setLoginUsername(merchantLoginParam.getMerchantLoginUserName());
//        loginPage.setLoginPassword(merchantLoginParam.getMerchantLoginPassword());
//        loginPage.setIndex(index++);
//
//        loginPage.login();
//
//        HomePage homePage = new HomePage();
//        homePage.setPage(page);
//        homePage.pageSearch();
//
//        loginPage.logout();
//
//    }
//
//}
