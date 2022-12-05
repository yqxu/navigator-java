package com.pingpongx.smb.monitor.biz.pages.fm;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.pingpongx.smb.monitor.dal.entity.constant.FMMainPages;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 首页
 */
@Slf4j
@Data
public class HomePage {

    private Page page;

    public HomePage() {

    }

    /**
     * 首页 菜单切换，确保当前是在福贸首页
     */
    public void switchPage(FMMainPages pageName) {
        switch (pageName.getPageName()) {
            case "人民币账户详情":
                page.locator("#cny-wallet-module").getByText("详情").click();
                break;
            case "首页":
                page.getByText("首页", new Page.GetByTextOptions().setExact(true)).click();
                break;
            case "外贸收款":
                page.locator("#payment-link").getByText("外贸收款").click();
                page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("付款人名称/收款账户")).click();
                break;
            case "外贸收款账户管理":
                page.locator("#receive-link").getByText("收款账户").hover();
                page.getByText("外贸收款账户管理").click();
                break;
            case "资金轨迹":
                page.locator("#receive-link").getByText("收款账户").hover();
                page.getByText("资金轨迹").click();
                break;
            case "合同订单":
                page.getByText("合同订单").click();
                break;
            case "账单收款":
                page.getByText("账单收款").click();
                break;
            case "外贸收款提现":
                boolean tiXianVisible = page.getByText("提现", new Page.GetByTextOptions().setExact(true)).isVisible();
                log.debug("提现按钮可见：{}", tiXianVisible);
                Locator.HoverOptions hoverOptions1 = new Locator.HoverOptions();
                hoverOptions1.setNoWaitAfter(true);
                page.getByText("提现", new Page.GetByTextOptions().setExact(true)).hover(hoverOptions1);
                page.getByText("外贸收款提现", new Page.GetByTextOptions().setExact(true)).click();
                break;
            case "人民币账户提现":
                page.getByText("提现", new Page.GetByTextOptions().setExact(true)).first().hover();
                page.getByText("人民币账户提现").click();
                break;
            case "发起付款":
                page.getByText("付款", new Page.GetByTextOptions().setExact(true)).first().hover();
                page.getByText("发起付款").click();
                break;
            case "收款人管理":
                page.getByText("付款", new Page.GetByTextOptions().setExact(true)).hover();
                page.getByText("收款人管理").click();
                break;
            case "外贸收款明细":
                boolean jiaoYiChaXunVisible = page.getByText("交易查询", new Page.GetByTextOptions().setExact(true)).isVisible();
                log.debug("交易查询按钮可见：{}", jiaoYiChaXunVisible);
                page.getByText("交易查询", new Page.GetByTextOptions().setExact(true)).hover();
                page.getByText("外贸收款明细").click();
                break;
            case "人民币账户明细":
                page.getByText("交易查询", new Page.GetByTextOptions().setExact(true)).hover();
                page.getByText("人民币账户明细").click();
                break;
            case "汇率风险":
                page.getByText("外汇", new Page.GetByTextOptions().setExact(true)).hover();
                page.getByText("汇率风险").click();
                break;
        }

    }

    /**
     * 悬浮鼠标到个人中心，点击菜单
     * @param menuName
     */
    public void personalInfo(String menuName) {
        page.locator(".person-warp").hover();
        page.getByText(menuName, new Page.GetByTextOptions().setExact(true)).click();
    }


}
