package com.pingpongx.smb.monitor.biz.job;

import com.alibaba.fastjson.JSON;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import com.pingpongx.job.core.handler.annotation.JobHandler;
import com.pingpongx.smb.monitor.biz.pages.fm.CNYWalletPage;
import com.pingpongx.smb.monitor.biz.pages.fm.HomePage;
import com.pingpongx.smb.monitor.biz.pages.fm.LoginPage;
import com.pingpongx.smb.monitor.dal.entity.uiprops.FMLoginParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component
@JobHandler
public class FMMonitor extends MonitorTemplateJob {

    @Resource
    private FMLoginParam fmLoginParam;

    @Override
    public void initEnv() {
        super.setHost(fmLoginParam.getMonitorFmHost());
        super.setDingGroup("FLOWMORE");
        super.setBusiness(this.getClass().getSimpleName());
    }

    @Override
    public void actions(Page page) {
        log.info("FMMonitor 参数信息：{}", JSON.toJSONString(fmLoginParam));

        LoginPage loginPage = new LoginPage();
        loginPage.setPage(page);
        loginPage.setLoginUrl(fmLoginParam.getFmLoginUrl());
        loginPage.setLoginUsername(fmLoginParam.getFmLoginUserName());
        loginPage.setLoginPassword(fmLoginParam.getFmLoginPassword());
        loginPage.login();

        HomePage homePage = new HomePage();
        homePage.setPage(page);
        homePage.pageSearch();

        CNYWalletPage cnyWalletPage = new CNYWalletPage();
        cnyWalletPage.setPage(page);
        cnyWalletPage.switchDetails("转账记录");
        cnyWalletPage.switchDetails("提现记录");
        cnyWalletPage.switchDetails("账单明细");
        cnyWalletPage.walletOperator("转入");
        Page.GoBackOptions goBackOptions = new Page.GoBackOptions();
        goBackOptions.setWaitUntil(WaitUntilState.DOMCONTENTLOADED);
        page.goBack(goBackOptions);
        cnyWalletPage.walletOperator("转账");
        page.goBack(goBackOptions);
        cnyWalletPage.walletOperator("提现");
        page.goBack(goBackOptions);
        cnyWalletPage.walletOperator("付款");
        page.goBack(goBackOptions);

        homePage.personalInfo("个人中心");
        homePage.personalInfo("代言人计划");
        homePage.personalInfo("我的优惠券");
        homePage.personalInfo("子账号管理");
        homePage.personalInfo("待办事项");

        loginPage.logout();
    }
}
