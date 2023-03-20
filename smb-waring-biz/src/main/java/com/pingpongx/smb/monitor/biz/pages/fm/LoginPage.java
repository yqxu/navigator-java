package com.pingpongx.smb.monitor.biz.pages.fm;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
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


    /**
     * 登录，登录后应是首页，如果是验证码输入页面，会报错
     */
    public void login() {
        Page.NavigateOptions options = new Page.NavigateOptions();
        options.setWaitUntil(WaitUntilState.DOMCONTENTLOADED);
//        options.setWaitUntil(WaitUntilState.NETWORKIDLE);
        options.setTimeout(60000);
        page.navigate(loginUrl, options);
        // 打印浏览器的 localStorage
        // page.context().storageState();

        page.getByPlaceholder("邮箱地址").click();
        page.getByPlaceholder("邮箱地址").fill(loginUsername);

        page.getByPlaceholder("登录密码").click();
        page.getByPlaceholder("登录密码").fill(loginPassword);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("登录")).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        // 福贸登录完成后会对用户进行菜单变更引导，但是这个没有做在后端，前端会在每次打开浏览器时进行引导，如果清除了缓存的话
        log.info("fm login finished");

        // 对福贸登录后可能出现的弹窗进行关闭
        if (waitElementExist(page.getByRole(AriaRole.DIALOG, new Page.GetByRoleOptions().setName("dialog")), 5000)) {
            page.getByRole(AriaRole.DIALOG, new Page.GetByRoleOptions().setName("dialog")).locator("i").click();
        }
        log.info("fm login finished and menu check finished");

    }

    public void logout() {
        page.locator(".person-warp").click();
        page.getByText("退出").click();
    }



}
