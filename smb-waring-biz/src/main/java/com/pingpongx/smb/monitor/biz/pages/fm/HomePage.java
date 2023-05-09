package com.pingpongx.smb.monitor.biz.pages.fm;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static com.pingpongx.smb.monitor.biz.util.PlayWrightUtils.waitElementExist;

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
    public void switchPage(String pageName) {
        switch (pageName) {
            case "人民币账户详情":
                page.locator("#cny-wallet-module").getByText("详情").click();
                break;
            case "首页":
                page.getByText("首页", new Page.GetByTextOptions().setExact(true)).click();
                // 处理首页中可能出现的问卷弹窗
                if (waitElementExist(page.getByRole(AriaRole.DIALOG, new Page.GetByRoleOptions().setName("dialog")).locator("i"), 600)) {
                    page.getByRole(AriaRole.DIALOG, new Page.GetByRoleOptions().setName("dialog")).locator("i").click();
                }
                break;
            case "外贸收款":
                if (page.getByText("接收验证码方式").isVisible()) {
                    throw new PlaywrightException("当前存在接收验证码方式元素，可能是有人修改了登录密码，需手动处理");
                }
                page.locator("#payment-link").getByText("外贸收款").first().click();
                break;
            case "收款账户管理":
                if (!page.getByText("外贸收款账户管理", new Page.GetByTextOptions().setExact(true)).isVisible()) {
                    page.getByText("外贸收款", new Page.GetByTextOptions().setExact(true)).first().hover();
                }
                page.getByText("收款账户管理").click();
                break;
            case "资金轨迹":
                if (!page.getByText("资金轨迹", new Page.GetByTextOptions().setExact(true)).isVisible()) {
                    page.getByText("外贸收款", new Page.GetByTextOptions().setExact(true)).first().hover();
                }
                page.getByText("资金轨迹").click();
                break;
            case "银行卡收款":
                if (!page.getByText("银行卡收款", new Page.GetByTextOptions().setExact(true)).isVisible()) {
                    page.getByText("外贸收款", new Page.GetByTextOptions().setExact(true)).first().hover();
                }
                page.getByText("银行卡收款").click();
                break;
            case "合同订单":
                if (!page.getByText("合同订单", new Page.GetByTextOptions().setExact(true)).isVisible()) {
                    page.getByText("外贸收款", new Page.GetByTextOptions().setExact(true)).first().hover();
                }
                page.getByText("合同订单", new Page.GetByTextOptions().setExact(true)).click();
                break;
            case "账单收款":
                log.info("账单收款，count:{}", page.locator("#app").getByText("账单收款", new Locator.GetByTextOptions().setExact(true)).count());
                page.locator("xpath=//div/span[contains(string(), '账单收款')]").hover();
                page.getByRole(AriaRole.TOOLTIP, new Page.GetByRoleOptions().setName("账单收款 应收账款统计")).getByText("账单收款").click();
                break;
            case "应收账款统计":
                page.locator("xpath=//div/span[contains(string(), '账单收款')]").hover();
                page.getByText("应收账款统计").click();
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
            case "供应商付款":
                page.getByText("付款", new Page.GetByTextOptions().setExact(true)).first().hover();
                page.getByText("供应商付款").last().click();
                break;
            case "收款人管理":
                page.getByText("付款", new Page.GetByTextOptions().setExact(true)).hover();
                page.getByText("收款人管理").last().click();
                break;
            case "退税服务":
                page.getByText("付款", new Page.GetByTextOptions().setExact(true)).hover();
                page.getByText("退税服务").last().click();
                break;
            case "服务商付款":
                page.getByText("付款", new Page.GetByTextOptions().setExact(true)).hover();
                page.getByText("服务商付款").last().click();
                break;
            case "外贸收款交易查询":
                page.getByText("交易查询", new Page.GetByTextOptions().setExact(true)).first().hover();
                page.getByText("外贸收款交易查询").click();
                break;
            case "挂单记录":
                page.getByText("交易查询", new Page.GetByTextOptions().setExact(true)).first().hover();
                page.getByText("挂单记录").click();
                break;
            case "人民币账户交易查询":
                page.getByText("交易查询", new Page.GetByTextOptions().setExact(true)).hover();
                // 人民币账户明细按钮，有时会不展示，需要前端协助修改
                if (page.getByText("人民币账户交易查询").isVisible()) {
                    page.getByText("人民币账户交易查询").click();
                }
                break;
            case "汇率风险":
                page.getByText("外汇", new Page.GetByTextOptions().setExact(true)).hover();
                page.getByText("汇率风险").click();
                break;
        }

    }

    /**
     * 页面遍历
     */
    public void pageSearch() {
        switchPage("外贸收款明细");
        switchPage("合同订单");
        switchPage("收款账户管理");
        switchPage("资金轨迹");

        switchPage("账单收款");
        switchPage("应收账款统计");

        switchPage("外贸收款提现");
        switchPage("人民币账户提现");

        switchPage("供应商付款");
        switchPage("收款人管理");
        switchPage("退税服务");
        switchPage("服务商付款");

        switchPage("外贸收款交易查询");
        switchPage("挂单记录");
        switchPage("人民币账户交易查询");

        switchPage("汇率风险");

        switchPage("首页");

        switchPage("人民币账户详情");
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
