package com.pingpongx.smb.monitor.biz.pages.merchant;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.*;
import com.pingpongx.smb.monitor.biz.exception.LoginException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.pingpongx.smb.monitor.biz.util.PlayWrightUtils.waitElementExist;

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
        page.navigate(loginUrl, options);
        // 如果当前打开的是us区的页面，目前发生在容器环境中，通过修改cookie中vuepack的值的方式，强制刷到cn区
        if (waitElementExist(page.getByText("Email"), 2000)) {
            List<Cookie> newCookies = new ArrayList<>(page.context().cookies());
            for (Cookie cookie : newCookies) {
                if (cookie.name.equals("vuepack")) {
                    cookie.value="%7B%22language%22%3A%22zh-CN%22%7D";
                }
            }
            page.context().clearCookies();
            page.context().addCookies(newCookies);
            page.reload();
            // log.info("merchant cookies:{}", JSON.toJSONString(page.context().cookies()));
        }

        page.getByText("邮箱登录").click();
        page.getByPlaceholder("输入您的登录邮箱").click();
        page.getByPlaceholder("输入您的登录邮箱").fill(loginUsername);
        page.getByPlaceholder("登录密码").click();
        page.getByPlaceholder("登录密码").fill(loginPassword);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("立即登录")).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        // 解决首页温馨提示弹2,3次的问题
        for (int i=0; i<3; i++) {
            if (waitElementExist(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Close")), 3000)) {
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Close")).click();
            }
        }
        // 等待登录完成
        if (!waitElementExist(page.getByText("首页", new Page.GetByTextOptions().setExact(true)), 3000)) {
            throw new LoginException("登录后，未能找到首页菜单，认定为登录失败");
        }
    }

    public void logout() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("账号中心")).hover();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("退出登录")).click();
    }
}
