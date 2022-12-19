package com.pingpongx.smb.monitor.biz.pages.fm;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
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
                if (page.getByText("接收验证码方式").isVisible()) {
                    throw new PlaywrightException("当前存在接收验证码方式元素，可能是有人修改了登录密码，需手动处理");
                }
                page.locator("#payment-link").getByText("外贸收款").click();
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
                log.info("提现按钮可见：{}", tiXianVisible);
                Locator.HoverOptions hoverOptions1 = new Locator.HoverOptions();
                hoverOptions1.setNoWaitAfter(true);
                page.getByText("提现", new Page.GetByTextOptions().setExact(true)).hover(hoverOptions1);
                page.getByText("外贸收款提现", new Page.GetByTextOptions().setExact(true)).click();
                break;
            case "人民币账户提现":
                page.getByText("提现", new Page.GetByTextOptions().setExact(true)).first().hover();
                if (page.getByText("人民币账户提现", new Page.GetByTextOptions().setExact(true)).isVisible()) {
                    page.getByText("人民币账户提现", new Page.GetByTextOptions().setExact(true)).click();
                }
                break;
            case "发起付款":
                page.getByText("付款", new Page.GetByTextOptions().setExact(true)).first().hover();
                if (page.getByRole(AriaRole.TOOLTIP).getByText("供应商付款").isVisible()) {
                    page.getByRole(AriaRole.TOOLTIP).getByText("供应商付款").hover();
                }
                log.info("发起付款元素数量:{}", page.getByText("发起付款").count());
                page.getByText("发起付款").last().click();
                break;
            case "收款人管理":
                page.getByText("付款", new Page.GetByTextOptions().setExact(true)).hover();
                if (page.getByRole(AriaRole.TOOLTIP).getByText("供应商付款").isVisible()) {
                    page.getByRole(AriaRole.TOOLTIP).getByText("供应商付款").hover();
                }
                page.getByText("收款人管理").last().click();
                break;
            case "外贸收款明细":
                boolean jiaoYiChaXunVisible = page.getByText("交易查询", new Page.GetByTextOptions().setExact(true)).isVisible();
                log.debug("交易查询按钮可见：{}", jiaoYiChaXunVisible);
                page.getByText("交易查询", new Page.GetByTextOptions().setExact(true)).hover();
                page.getByText("外贸收款明细").click();
                break;
            case "人民币账户明细":
                page.getByText("交易查询", new Page.GetByTextOptions().setExact(true)).hover();
                log.info("人民币账户明细元素数量：{}", page.getByText("人民币账户明细").count());
                // 人民币账户明细按钮，有时会不展示，需要前端协助修改
                if (page.getByText("人民币账户明细").isVisible()) {
                    page.getByText("人民币账户明细").click();
                }
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
