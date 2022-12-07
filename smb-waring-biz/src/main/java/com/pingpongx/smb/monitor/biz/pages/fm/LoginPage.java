package com.pingpongx.smb.monitor.biz.pages.fm;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
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


    /**
     * 登录，登录后应是首页，如果是验证码输入页面，会报错
     */
    public void login() {
        Page.NavigateOptions options = new Page.NavigateOptions();
        options.setWaitUntil(WaitUntilState.DOMCONTENTLOADED);
//        options.setWaitUntil(WaitUntilState.NETWORKIDLE);
        options.setTimeout(60000);
        page.navigate(loginUrl, options);

        page.getByPlaceholder("邮箱地址").click();
        page.getByPlaceholder("邮箱地址").fill(loginUsername);

        page.getByPlaceholder("登录密码").click();
        page.getByPlaceholder("登录密码").fill(loginPassword);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("登录")).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

    }

    public void logout() {
        page.locator(".person-warp").click();
        page.getByText("退出").click();
    }



}
