package com.pingpongx.smb.monitor.biz.pages.merchant;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class LoginPage {

    private Page page;
    private String loginUrl;
    private String loginUsername;
    private String loginPassword;
    private int index;

    public LoginPage() {

    }

    public void login() {
        Page.NavigateOptions options = new Page.NavigateOptions();
        options.setWaitUntil(WaitUntilState.DOMCONTENTLOADED);
        page.navigate(loginUrl, options);

        page.getByText("邮箱登录").click();
        page.getByPlaceholder("输入您的登录邮箱").click();
        String[] strings = new String[]{"wangqq@pingpongx.com", "wangqq5@pingpongx.com", "ding.yin.g@163.com", "lihx1@pingpongx.com"};

//        page.getByPlaceholder("输入您的登录邮箱").fill(loginUsername);
        index = index % strings.length;
        log.info("login index: {}", index);
        page.getByPlaceholder("输入您的登录邮箱").fill(strings[index]);
        page.getByPlaceholder("登录密码").click();
        page.getByPlaceholder("登录密码").fill(loginPassword);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("立即登录")).click();
        // 等待登录完成
        Locator.WaitForOptions waitForOptions = new Locator.WaitForOptions();
        waitForOptions.setState(WaitForSelectorState.VISIBLE);
        waitForOptions.setTimeout(30 * 1000);
        page.getByText("首页").waitFor(waitForOptions);
        int selectorCount = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("首页")).count();
        log.info("主站首页：{}", selectorCount);
        if (!(selectorCount > 0)) {
            throw new PlaywrightException("可能没登录成功");
        }
    }

    public void logout() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("账号中心")).hover();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("退出登录")).click();
    }
}
