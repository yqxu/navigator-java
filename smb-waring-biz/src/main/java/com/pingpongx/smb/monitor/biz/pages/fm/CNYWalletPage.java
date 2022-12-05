package com.pingpongx.smb.monitor.biz.pages.fm;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CNYWalletPage {

    private Page page;

    public CNYWalletPage() {

    }

    /**
     * 切换展示 账单明细
     */
    public void switchDetails(String menuName) {
        switch (menuName) {
            case "转账记录":
                page.getByText("转账记录", new Page.GetByTextOptions().setExact(true)).click();
                break;
            case "提现记录":
                page.getByText("提现记录", new Page.GetByTextOptions().setExact(true)).click();
                break;
            case "账单明细":
                page.getByText("账单明细", new Page.GetByTextOptions().setExact(true)).click();
                break;
        }
    }

    /**
     * 钱包操作，转入，转账，提现，付款
     */
    public void walletOperator(String menuName) {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(menuName)).click();
    }

}
