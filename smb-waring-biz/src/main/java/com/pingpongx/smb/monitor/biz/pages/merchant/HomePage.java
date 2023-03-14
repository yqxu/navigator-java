package com.pingpongx.smb.monitor.biz.pages.merchant;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static com.pingpongx.smb.monitor.biz.util.PlayWrightUtils.waitElementExist;

@Data
@Slf4j
public class HomePage {

    private Page page;

    public HomePage() {

    }

    public void pageSwitch(String menuName) {
        switch (menuName) {
            case "店铺管理":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("店铺管理")).isVisible()) {
                    page.getByText("平台收款").first().hover();
                }
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("店铺管理")).click();
                break;
            case "店铺迁移":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("店铺迁移")).isVisible()) {
                    page.getByText("平台收款").first().hover();
                }
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("店铺迁移")).click();
                break;
            case "店铺托管":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("店铺托管")).isVisible()) {
                    page.getByText("平台收款").first().hover();
                }
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("店铺托管")).click();
                break;
            case "店铺解绑":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("店铺解绑")).isVisible()) {
                    page.getByText("平台收款").first().hover();
                }
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("店铺解绑")).click();
                break;
            case "平台收款明细":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("平台收款明细")).isVisible()) {
                    page.getByText("明细查询").first().hover();
                }
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("平台收款明细")).click();
                break;
            case "人民币账户明细":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("人民币账户明细")).isVisible()) {
                    page.getByText("明细查询").first().hover();
                }
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("人民币账户明细")).click();
                break;
            case "供应商付款":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("供应商付款")).isVisible()) {
                    page.getByText("付款服务").first().hover();
                }
                page.getByText("供应商付款").first().hover();
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("供应商付款")).first().click();
                break;
            case "供应商付款管理":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("供应商付款管理")).isVisible()) {
                    page.getByText("付款服务").first().hover();
                }
                page.getByText("供应商付款").first().hover();
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("供应商付款管理")).click();
                break;
            case "供应商管理":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("供应商管理")).isVisible()) {
                    page.getByText("付款服务").first().hover();
                }
                page.getByText("供应商付款").first().hover();
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("供应商管理")).click();
                break;
            case "邮箱付款":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("邮箱付款")).isVisible()) {
                    page.getByText("付款服务").first().hover();
                }
                page.getByText("供应商付款").first().hover();
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("邮箱付款")).click();
                break;
            case "服务商付款":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("服务商付款")).isVisible()) {
                    page.getByText("付款服务").first().hover();
                }
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("服务商付款")).click();
                break;
            case "退税服务":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("退税服务")).isVisible()) {
                    page.getByText("付款服务").first().hover();
                }
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("退税服务")).click();
                break;
            case "VAT缴税":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("VAT缴税")).isVisible()) {
                    page.getByText("付款服务").first().hover();
                }
                page.getByText("VAT服务").first().hover();
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("VAT缴税")).click();
                break;
            case "VAT退税":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("VAT退税")).isVisible()) {
                    page.getByText("付款服务").first().hover();
                }
                page.getByText("VAT服务").first().hover();
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("VAT退税")).click();
                break;
            case "开店通":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("开店通")).isVisible()) {
                    page.getByText("更多服务").first().hover();
                }
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("开店通")).click();
                break;
            case "合作收款":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("合作收款")).isVisible()) {
                    page.getByText("更多服务").first().hover();
                }
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("合作收款")).click();
                if (page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Close")).isVisible()) {
                    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Close")).click();
                }
                break;
            case "保险信息服务":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("保险信息服务")).isVisible()) {
                    page.getByText("更多服务").first().hover();
                }
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("保险信息服务")).click();
                break;
            case "诚e赊":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("诚e赊")).isVisible()) {
                    page.getByText("更多服务").first().hover();
                }
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("诚e赊")).click();
                break;
            case "对账助手":
                if (!page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("对账助手")).isVisible()) {
                    page.getByText("更多服务").first().hover();
                }
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("对账助手")).click();
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("返回PingPong")).click();
                waitElementExist(page.getByText("首页"), 400);
                break;
        }
    }

    public void profileSwitch(String menuName) {
        if (!page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(menuName)).isVisible()) {
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("账号中心")).hover();
        }
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(menuName)).click();
    }

    public void inviteRecommend(String menuName) {
        if (!page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(menuName)).isVisible()) {
            page.locator("#app").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("推荐有礼")).hover();
        }
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(menuName)).click();
    }

    public void pageSearch() {
        pageSwitch("店铺管理");
        pageSwitch("店铺迁移");
        pageSwitch("店铺托管");
        pageSwitch("店铺解绑");

        pageSwitch("平台收款明细");
        pageSwitch("人民币账户明细");

        pageSwitch("供应商付款");
        pageSwitch("供应商付款管理");
        pageSwitch("供应商管理");
        pageSwitch("邮箱付款");
        pageSwitch("服务商付款");
        pageSwitch("退税服务");
        pageSwitch("VAT缴税");
        pageSwitch("VAT退税");

        pageSwitch("开店通");
        pageSwitch("合作收款");
        pageSwitch("保险信息服务");
        pageSwitch("对账助手");
        pageSwitch("诚e赊");

        profileSwitch("我的账号");
        profileSwitch("实名认证");
        profileSwitch("提现账户");
        profileSwitch("持有人管理");
        profileSwitch("我的智+");
        profileSwitch("我的优惠券");
        profileSwitch("订单管理");
        profileSwitch("子账号管理");

        inviteRecommend("邀好友享福利");
        inviteRecommend("服务商推荐有礼");

    }


}
