package com.pingpongx.smb.monitor.biz.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import com.microsoft.playwright.options.Timing;
import com.microsoft.playwright.options.WaitUntilState;
import com.pingpongx.smb.monitor.biz.pages.fm.CNYWalletPage;
import com.pingpongx.smb.monitor.biz.pages.fm.HomePage;
import com.pingpongx.smb.monitor.biz.pages.fm.LoginPage;
import com.pingpongx.smb.monitor.dal.entity.constant.BusinessLine;
import com.pingpongx.smb.monitor.dal.entity.constant.FMMainPages;
import com.pingpongx.smb.monitor.dal.entity.constant.MonitorEnv;
import com.pingpongx.smb.monitor.dal.entity.dataobj.ApiDetail;
import com.pingpongx.smb.monitor.dal.entity.dataobj.TaskRecord;
import com.pingpongx.smb.monitor.dal.entity.uiprops.HostParam;
import com.pingpongx.smb.monitor.dal.entity.uiprops.LoginParam;
import com.pingpongx.smb.monitor.dal.mapper.ApiDetailMapper;
import com.pingpongx.smb.monitor.dal.mapper.TaskRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.pingpongx.smb.monitor.biz.util.TimeUtils.getFormattedTime;


@Slf4j
@Component
@EnableScheduling
public class FMMonitor {

    @Resource
    private TaskRecordMapper taskRecordMapper;

    @Resource
    private ApiDetailMapper apiDetailMapper;

    @Autowired
    private LoginParam loginParam;

    @Autowired
    private HostParam hostParam;


    @Scheduled(fixedDelay = 60000)
    public void monitorFM() {
        log.info("开始时间：{}, 当前monitor的环境：{}", getFormattedTime(), hostParam.getMonitorEnv());
        Consumer<Response> listener = null;
        Playwright playwright = null;
        Browser browser = null;
        BrowserContext context = null;
        Page page = null;
        APIRequestContext apiRequestContext = null;
        Browser.NewContextOptions newContextOptions = new Browser.NewContextOptions().setViewportSize(1440, 875);

        try {
            playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
//                .setHeadless(false)
//                .setDevtools(true)
                    .setSlowMo(1800));
            // 不同的context的配置，理论上可能是一样的，例如浏览器的尺寸
            context = browser.newContext(newContextOptions);
            page = context.newPage();
            page.setDefaultTimeout(60000);
            apiRequestContext = playwright.request().newContext(new APIRequest.NewContextOptions());

            listener = monitorPageRequest(apiRequestContext, page);

            LoginPage loginPage = new LoginPage();
            loginPage.setPage(page);
            loginPage.setLoginUrl(loginParam.getFmLoginUrl());
            loginPage.setLoginUsername(loginParam.getFmLoginUserName());
            loginPage.setLoginPassword(loginParam.getFmLoginPassword());
            loginPage.login();

            HomePage homePage = new HomePage();
            homePage.setPage(page);
            homePage.switchPage(FMMainPages.WAI_MAO_SHOU_KUAN);
            homePage.switchPage(FMMainPages.WAI_MAO_SHOU_KUAN_ZHANG_HU_GUAN_LI);
            homePage.switchPage(FMMainPages.ZI_JING_HUI_JI);
            homePage.switchPage(FMMainPages.HE_TONG_DING_DANG);
            homePage.switchPage(FMMainPages.ZHANG_DAN_SHOU_KUAN);
            homePage.switchPage(FMMainPages.WAI_MAO_SHOU_KUAN_TI_XIAN);
            homePage.switchPage(FMMainPages.REN_MING_BI_ZHANG_HU_TI_XIAN);
            homePage.switchPage(FMMainPages.FA_QI_FU_KUAN);
            homePage.switchPage(FMMainPages.SHOU_KUAN_REN_GUAN_LI);
            homePage.switchPage(FMMainPages.WAI_MAO_SHOU_KUAN_MING_XI);
            homePage.switchPage(FMMainPages.REN_MING_BI_ZHANG_HU_MING_XI);
            homePage.switchPage(FMMainPages.HUI_LV_FENG_XIAN);
            homePage.switchPage(FMMainPages.SHOU_YE);
            homePage.switchPage(FMMainPages.WALLET_DETAIL);

            CNYWalletPage cnyWalletPage = new CNYWalletPage();
            cnyWalletPage.setPage(page);
            cnyWalletPage.switchDetails("转账记录");
            cnyWalletPage.switchDetails("提现记录");
            cnyWalletPage.switchDetails("账单明细");
            cnyWalletPage.walletOperator("转入");
            Page.GoBackOptions goBackOptions = new Page.GoBackOptions();
            goBackOptions.setWaitUntil(WaitUntilState.DOMCONTENTLOADED);
            page.goBack(goBackOptions);
            cnyWalletPage.walletOperator("转账");
            page.goBack(goBackOptions);
            cnyWalletPage.walletOperator("提现");
            page.goBack(goBackOptions);
            cnyWalletPage.walletOperator("付款");
            page.goBack(goBackOptions);

            homePage.personalInfo("个人中心");
            homePage.personalInfo("代言人计划");
            homePage.personalInfo("我的优惠券");
            homePage.personalInfo("子账号管理");
            homePage.personalInfo("待办事项");

            loginPage.logout();

            // 执行成功，将结果写入库表
            insertRecord("success", "na");
        } catch (Exception e) {
            // 执行失败，截个图的，可以通过命令将文件复制出容器查看：curl -F 'x=@/tmp/ui-monitor/20221130101603.png' file.pingpongx.com/disk
            // 打开地址：https://file.pingpongx.com/disk
            if (page != null) {
                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("/tmp/ui-monitor/" + getFormattedTime() + ".png")));
            }
            log.error("monitorFM exception, message size: " + e.getMessage());
            // 发送钉钉告警
            // if (apiRequestContext != null) {
                // sendWarnMessage(apiRequestContext , e.getMessage());
            // }
            // 执行失败，写入库表
            insertRecord("failed", e.getMessage());
        } finally {
            if (listener != null) {
                page.offResponse(listener);
            }
            Page.CloseOptions closeOptions = new Page.CloseOptions();
            closeOptions.setRunBeforeUnload(true);
            // 不能直接关闭，可能退出登录了，还有请求在进行中，会导致 page.onResponse监控的接口数据 报错
            if (page != null) {
                page.close(closeOptions);
            }
            if (context != null) {
                context.close();
            }
            if (browser != null) {
                browser.close();
            }
            if (playwright != null) {
                playwright.close();
            }
        }

    }

    /**
     * 调用开发的接口发送钉钉告警
     * @param message
     */
    public void sendWarnMessage(APIRequestContext apiRequestContext, String message) {
        Map<String, String> data = new HashMap<>();
        data.put("appName", "ui-monitor");
        data.put("className", this.getClass().getSimpleName());
        data.put("content", message);
        data.put("hostName", loginParam.getMonitorFmHost());
        data.put("time", getFormattedTime());

        // 如果当前是生产环境，发告警出来
        if (hostParam.getMonitorEnv().equals(MonitorEnv.PROD.getMonitorEnv())) {
            apiRequestContext.post("https://smb-warning.pingpongx.com/v2/alert/FLOWMORE", RequestOptions.create().setData(data));
        }

    }

    private void insertRecord(String result, String failReason) {
        TaskRecord record = new TaskRecord();
        record.setEnvironment(hostParam.getMonitorEnv());
        record.setBusinessLine(BusinessLine.FM.getBusinessLine());
        record.setFailReason(failReason);
        record.setTaskResult(result);
        // log.info("record is : {}", JSON.toJSONString(record));
        taskRecordMapper.insert(record);
    }

    private void saveResponseDetail(Response response, int code) {
        ApiDetail apiDetail = new ApiDetail();
        apiDetail.setRequestUrl(response.request().url());
        apiDetail.setResponse(response.text());
        apiDetail.setResponseCode(String.valueOf(code));
        Timing timing = response.request().timing();
        apiDetail.setMethod(response.request().method());
        apiDetail.setUsedTime(Double.toString(timing.responseEnd - timing.connectStart));
        // 写入接口记录表
        apiDetailMapper.insert(apiDetail);
    }

    /**
     * 监听页面接口请求，如果响应信息中有失败的，报错的，需要钉钉告警
     * @param page
     */
    private Consumer<Response> monitorPageRequest(APIRequestContext apiRequestContext, Page page) {
        Consumer<Response> listener = response -> {
            try {
                if (response.request().url().contains(loginParam.getMonitorFmHost()) && response.request().url().contains("api")) {
                    String resText = response.text();
                    if (StringUtils.hasLength(resText)) {
                        int code = JSONPath.read(resText, "$.code", Integer.class);
                        // todo 如果响应信息的内容长度过长需要告警吗？例如有一个接口的响应内容超过64K，目前超64K插入库表会报错的
                        // saveResponseDetail(response, code);
                        // 后续可能需要考虑code的判断条件，比如如果服务端错误，是5开头这种，
                        if (code != 0 && code != 401) {
                            saveResponseDetail(response, code);
                            // 发送告警，50004是服务端超时错误，暂时不发告警
                            if (code != 50004) {
                                sendWarnMessage(apiRequestContext,"api monitor error\nurl:"+ response.request().url() + "\n,res:" + resText);
                            }
                            log.error("api monitor error\nurl:"+ response.request().url() + "\n,res:" + resText);
                        }
                    }
                }
            } catch (Throwable throwable) {
                // 可能会因为服务端没有给响应信息而中断
                if (!throwable.getMessage().contains("No resource with given identifier found")) {
                    log.error("monitor url: {}, error:{}", response.request().url(), throwable.getMessage());
                }
            }
        };
        page.onResponse(listener);


        // 忽略图片请求
        page.route("**/*.{png,jpg,jpeg}", Route::abort);
//        page.route("**/*", route -> {
//            if ("image".equals(route.request().resourceType()))
//                route.abort();
//            else
//                route.resume();
//        });
        return listener;
    }
}
