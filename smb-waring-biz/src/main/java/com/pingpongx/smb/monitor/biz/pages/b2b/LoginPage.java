package com.pingpongx.smb.monitor.biz.pages.b2b;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
        Page.NavigateOptions options = new Page.NavigateOptions();
        options.setWaitUntil(WaitUntilState.DOMCONTENTLOADED);
        options.setTimeout(20000);
        page.navigate(loginUrl, options);

        page.getByPlaceholder("邮箱地址").click();
        page.getByPlaceholder("邮箱地址").fill(loginUsername);
    }
}
