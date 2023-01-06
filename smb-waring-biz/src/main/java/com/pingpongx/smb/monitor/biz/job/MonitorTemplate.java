package com.pingpongx.smb.monitor.biz.job;

import com.alibaba.fastjson.JSONPath;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import com.microsoft.playwright.options.Timing;
import com.pingpongx.smb.monitor.dal.entity.constant.BusinessLine;
import com.pingpongx.smb.monitor.dal.entity.constant.MonitorEnv;
import com.pingpongx.smb.monitor.dal.entity.dataobj.ApiDetail;
import com.pingpongx.smb.monitor.dal.entity.dataobj.TaskRecord;
import com.pingpongx.smb.monitor.dal.entity.uiprops.MonitorEnvParam;
import com.pingpongx.smb.monitor.dal.mapper.ApiDetailMapper;
import com.pingpongx.smb.monitor.dal.mapper.TaskRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.pingpongx.smb.monitor.biz.util.TimeUtils.getFormattedTime;

@Slf4j
public abstract class MonitorTemplate {

    @Resource
    private TaskRecordMapper taskRecordMapper;

    @Resource
    private ApiDetailMapper apiDetailMapper;

    @Autowired
    private MonitorEnvParam monitorEnvParam;

    @Autowired
    private Playwright playwright;

    private String host;
    private String dingGroup;

    public void setDingGroup(String dingGroup) {
        this.dingGroup = dingGroup;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public abstract void actions(Page page);

    @Scheduled(fixedDelay = 180000)
    public void monitor() {
        log.info("hostParam.getEnable():{}", monitorEnvParam.getEnable());
        if (monitorEnvParam.getEnable().equalsIgnoreCase(Boolean.FALSE.toString())) {
            return;
        }
        log.info("开始时间：{}, 当前monitor的环境：{}", getFormattedTime(), monitorEnvParam.getMonitorEnv());
        Consumer<Response> listener = null;
        Browser browser = null;
        BrowserContext context = null;
        Page page = null;
        APIRequestContext apiRequestContext = null;
        Browser.NewContextOptions newContextOptions = new Browser.NewContextOptions().setViewportSize(1440, 875);

        try {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHandleSIGHUP(true)
                    .setHandleSIGINT(true)
                    .setHandleSIGTERM(true)
//                .setHeadless(false)
//                .setDevtools(true)
                    .setSlowMo(2200));
            // 不同的context的配置，理论上是一样的，例如浏览器的尺寸
            context = browser.newContext(newContextOptions);
            page = context.newPage();
            page.setDefaultTimeout(60000);
            page.setDefaultNavigationTimeout(60000);
            apiRequestContext = playwright.request().newContext(new APIRequest.NewContextOptions());

            listener = monitorPageRequest(apiRequestContext, page);

            actions(page);

            // 执行成功，将结果写入库表
            insertRecord("success", "na");
        } catch (Exception e) {
            // 执行失败，截个图的，可以通过命令将文件复制出容器查看：curl -F 'x=@/tmp/ui-monitor/20221209070003.png' file.pingpongx.com/disk
            // 打开地址：https://file.pingpongx.com/disk
            if (page != null) {
                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("/tmp/ui-monitor/" + getFormattedTime() + ".png")));
            }
            log.warn("monitor, message size: " + e.getMessage());
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
                page = null;
            }
            if (apiRequestContext != null) {
                apiRequestContext.dispose();
            }
            if (context != null) {
                context.close();
                context = null;
            }
            if (browser != null) {
                browser.close();
                browser = null;
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
        data.put("hostName", host);
        data.put("time", getFormattedTime());

        // 如果当前是生产环境，发告警出来
        if (monitorEnvParam.getMonitorEnv().equals(MonitorEnv.PROD.getMonitorEnv())) {
            apiRequestContext.post("https://smb-warning.pingpongx.com/v2/alert/" + dingGroup, RequestOptions.create().setData(data));
        }
    }

    private void insertRecord(String result, String failReason) {
        TaskRecord record = new TaskRecord();
        record.setEnvironment(monitorEnvParam.getMonitorEnv());
        record.setBusinessLine(BusinessLine.FM.getBusinessLine());
        // 在失败原因中截取字串，避免插表报错
        int lastIndex = Math.min(failReason.length(), 8191);
        record.setFailReason(failReason.substring(0, lastIndex));
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
                if (response.request().url().contains(host) && response.request().url().contains("api")) {
                    String resText = response.text();
                    if (StringUtils.hasLength(resText)) {
                        int code = JSONPath.read(resText, "$.code", Integer.class);
                        // todo 如果响应信息的内容长度过长需要告警吗？例如有一个接口的响应内容超过64K，目前超64K插入库表会报错的
                        // saveResponseDetail(response, code);
                        // 后续可能需要考虑code的判断条件，比如如果服务端错误，是5开头这种，
                        // 20404 40401 主站的code 码
                        if (code != 0 && code != 401 && code != 20404 && code != 40401) {
                            saveResponseDetail(response, code);
                            // 发送告警，50004是服务端超时错误，暂时不发告警
                            if (code != 50004) {
                                sendWarnMessage(apiRequestContext,"api monitor error\nurl:"+ response.request().url() + "\n,res:" + resText);
                            }
                            log.warn("api monitor error url:"+ response.request().url() + ",res:" + resText);
                        }
                    }
                }
            } catch (Throwable throwable) {
                // 可能会因为服务端没有给响应信息而中断
                if (throwable.getMessage() == null) {
                    log.warn("throwable.getMessage() is null");
                }
                if (throwable.getMessage() != null && !throwable.getMessage().contains("No resource with given identifier found")) {
                    log.warn("monitor url: {}, error:{}", response.request().url(), throwable.getMessage());
                }
            }
        };
        page.onResponse(listener);


        // 忽略图片请求
//        page.route("**/*.{png,jpg,jpeg}", Route::abort);
//        page.route("**/*", route -> {
//            if ("image".equals(route.request().resourceType()))
//                route.abort();
//            else
//                route.resume();
//        });
        // 解决容器环境中访问 https://flowmore.pingpongx.com/api/front/v2/auth/token 拿不到响应数据的问题
//        page.route("**/*", route -> {
//            // Override headers
//            Map<String, String> headers = new HashMap<>(route.request().headers());
//            headers.put("X-Forwarded-For", "47.96.196.247");
//            route.resume(new Route.ResumeOptions().setHeaders(headers));
//        });
        return listener;
    }

}
