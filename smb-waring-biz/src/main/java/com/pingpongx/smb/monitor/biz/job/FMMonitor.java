package com.pingpongx.smb.monitor.biz.job;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import com.pingpongx.smb.monitor.biz.pages.fm.CNYWalletPage;
import com.pingpongx.smb.monitor.biz.pages.fm.HomePage;
import com.pingpongx.smb.monitor.biz.pages.fm.LoginPage;
import com.pingpongx.smb.monitor.dal.entity.constant.FMMainPages;
import com.pingpongx.smb.monitor.dal.entity.uiprops.FMLoginParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@EnableScheduling
public class FMMonitor extends MonitorTemplate {

    @Autowired
    private FMLoginParam fmLoginParam;

    @Override
    public void actions(Page page) {
        super.setHost(fmLoginParam.getMonitorFmHost());
        super.setDingGroup("FLOWMORE");

        LoginPage loginPage = new LoginPage();
        loginPage.setPage(page);
        loginPage.setLoginUrl(fmLoginParam.getFmLoginUrl());
        loginPage.setLoginUsername(fmLoginParam.getFmLoginUserName());
        loginPage.setLoginPassword(fmLoginParam.getFmLoginPassword());
        loginPage.login();

        HomePage homePage = new HomePage();
        homePage.setPage(page);
        homePage.switchPage(FMMainPages.WAI_MAO_SHOU_KUAN);
        homePage.switchPage(FMMainPages.WAI_MAO_SHOU_KUAN_ZHANG_HU_GUAN_LI);
        homePage.switchPage(FMMainPages.ZI_JING_HUI_JI);
        homePage.switchPage(FMMainPages.HE_TONG_DING_DANG);
        homePage.switchPage(FMMainPages.ZHANG_DAN_SHOU_KUAN);
        homePage.switchPage(FMMainPages.WAI_MAO_SHOU_KUAN_TI_XIAN);
        homePage.switchPage(FMMainPages.REN_MING_BI_ZHANG_HU_TI_XIAN);
        homePage.switchPage(FMMainPages.FA_QI_FU_KUAN);
        homePage.switchPage(FMMainPages.SHOU_KUAN_REN_GUAN_LI);
        homePage.switchPage(FMMainPages.WAI_MAO_SHOU_KUAN_MING_XI);
        homePage.switchPage(FMMainPages.REN_MING_BI_ZHANG_HU_MING_XI);
        homePage.switchPage(FMMainPages.HUI_LV_FENG_XIAN);
        homePage.switchPage(FMMainPages.SHOU_YE);
        homePage.switchPage(FMMainPages.WALLET_DETAIL);

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
