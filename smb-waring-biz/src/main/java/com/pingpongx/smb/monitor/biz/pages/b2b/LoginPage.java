package com.pingpongx.smb.monitor.biz.pages.b2b;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import com.pingpongx.smb.monitor.biz.exception.LoginException;
import com.pingpongx.smb.monitor.biz.exception.ScriptException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static com.pingpongx.smb.monitor.biz.util.PlayWrightUtils.waitElementExist;

/**
 * 登录页
 */
@Data
@Slf4j
public class LoginPage {

    private Page page;
    private String loginUrl;
    private String loginUsername;
    private String loginPassword;

    public LoginPage() {

    }

    public void login() {
        try {
            Page.NavigateOptions options = new Page.NavigateOptions();
            options.setWaitUntil(WaitUntilState.DOMCONTENTLOADED);
            options.setTimeout(150000);
            page.navigate(loginUrl, options);
        } catch (Exception e) {
            throw new ScriptException("网络差？150秒内未能成功打开b2b登录页");
        }

        if (waitElementExist(page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email")), 20 * 1000)) {
            Page.ReloadOptions reloadOptions = new Page.ReloadOptions();
            reloadOptions.setTimeout(60 * 1000);
            reloadOptions.setWaitUntil(WaitUntilState.DOMCONTENTLOADED);
            page.reload();
            log.info("reload b2b login page finished");
        }

        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email")).fill(loginUsername);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).fill(loginPassword);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        if (!waitElementExist(page.getByText("DASHBOARD", new Page.GetByTextOptions().setExact(true)), 30000)) {
            throw new LoginException("登录后，未能找到首页菜单，认定为登录失败");
        }

    }
}
