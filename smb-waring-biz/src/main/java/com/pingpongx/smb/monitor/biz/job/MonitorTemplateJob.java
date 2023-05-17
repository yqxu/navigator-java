package com.pingpongx.smb.monitor.biz.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.HarMode;
import com.microsoft.playwright.options.RequestOptions;
import com.microsoft.playwright.options.Timing;
import com.pingpongx.job.core.biz.model.ReturnT;
import com.pingpongx.job.core.handler.IJobHandler;
import com.pingpongx.smb.monitor.biz.exception.LoginException;
import com.pingpongx.smb.monitor.biz.util.TimeUtils;
import com.pingpongx.smb.monitor.dal.entity.constant.BusinessLine;
import com.pingpongx.smb.monitor.dal.entity.constant.MonitorEnv;
import com.pingpongx.smb.monitor.dal.entity.dataobj.ApiDetail;
import com.pingpongx.smb.monitor.dal.entity.dataobj.TaskRecord;
import com.pingpongx.smb.monitor.dal.entity.uiprops.MonitorEnvParam;
import com.pingpongx.smb.monitor.dal.mapper.ApiDetailMapper;
import com.pingpongx.smb.monitor.dal.mapper.TaskRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static com.pingpongx.smb.monitor.biz.util.DingUtils.sendApiMonitorResultMsg;
import static com.pingpongx.smb.monitor.biz.util.DingUtils.sendUIMonitorResultMsg;
import static com.pingpongx.smb.monitor.biz.util.PlayWrightUtils.initUtil;
import static com.pingpongx.smb.monitor.biz.util.RegUtils.extractStringByReg;
import static com.pingpongx.smb.monitor.biz.util.TimeUtils.getFormattedTime;

/**
 * 问题：
 * 1. 主站监控的是ro环境，不是生产
 * 2. 主站有时候打开登录页，是空白页面，刷不出登录页
 * 3. 福贸登录时，有时会触发类似ddos拦截，导致响应信息拿不到数据 => 找光明加了公网ip的白名单
 */
@Slf4j
public abstract class MonitorTemplateJob extends IJobHandler {

    @Resource
    private TaskRecordMapper taskRecordMapper;

    @Resource
    private ApiDetailMapper apiDetailMapper;

    @Resource
    private MonitorEnvParam monitorEnvParam;

    private String host;
    private String dingGroup;
    private String business;
    private Page page;
    private String phoneNumber;
    private String loginSwitch;

    public void setLoginSwitch(String loginSwitch) {
        this.loginSwitch = loginSwitch;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDingGroup(String dingGroup) {
        this.dingGroup = dingGroup;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public Page getPage() {
        return this.page;
    }

    public abstract void initEnv();

    public abstract void login();

    public abstract void actions();

    public abstract void logout();

    public void clearLocalResult(Path path) {
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                //遍历删除文件
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
                //遍历删除目录
                public FileVisitResult postVisitDirectory(Path dir,IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ReturnT<String> monitor() {
        log.info("hostParam.getEnable():{}", monitorEnvParam.getEnable());
        if (monitorEnvParam.getEnable().equalsIgnoreCase(Boolean.FALSE.toString())) {
            return new ReturnT<>( "监控开关未打开");
        }
        initEnv();
        String jobStartTime = getFormattedTime();
        log.info("开始时间：{}，当前monitor的环境：{}，当前业务线：{}", jobStartTime, monitorEnvParam.getMonitorEnv(), business);
        Playwright playwright = null;
        Consumer<Response> listener = null;
        Browser browser = null;
        BrowserContext context = null;
        APIRequestContext apiRequestContext = null;
        Path localResultPath = Paths.get("/tmp/ui-monitor/ui-monitor-" + getFormattedTime());
        ReturnT<String> jobResult = null;
        Browser.NewContextOptions newContextOptions = new Browser.NewContextOptions()
                // 因为福贸的业务有向导弹窗，弹出来比较恶心，而且是前端记忆的，所以在启动浏览器时把这个填入localStorage里面
                .setStorageState("{\"origins\":[{\"origin\":\"" + host + "\",\"localStorage\":[{\"name\":\"guideStep\",\"value\":\"{\\\"haveKyc\\\":true,\\\"vaGuide\\\":true,\\\"vaUseGuide\\\":true,\\\"firstInbound\\\":false,\\\"inboundGuide1\\\":false,\\\"inboundGuide2\\\":false,\\\"inboundGuide3\\\":false}\"},{\"name\":\"LockExchangeGuide\",\"value\":\"1\"},{\"name\":\"menuV2Tips\",\"value\":\"1\"}]}]}")
                .setViewportSize(1440, 875)
                .setRecordVideoSize(576, 350)
                .setRecordVideoDir(localResultPath)
                .setRecordHarPath(Paths.get(localResultPath.toString() + "/harFile.har"))
                .setRecordHarMode(HarMode.MINIMAL)
                ;
        // 如果是主站的，只录主站的登录，如果是福贸的，只录福贸的登录
        if (host.contains("flowmore")) {
            newContextOptions.setRecordHarUrlFilter(host + "/api/front/v2/auth/token");
        } else {
            newContextOptions.setRecordHarUrlFilter(host + "/api/user/web/login");
        }

        try {
            playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHandleSIGHUP(true)
                    .setHandleSIGINT(true)
                    .setHandleSIGTERM(true)
                    .setHeadless(true)
                    .setDevtools(false)
                    .setSlowMo(2200));
            // 不同的context的配置，理论上是一样的，例如浏览器的尺寸
            context = browser.newContext(newContextOptions);
            context.setDefaultTimeout(20 * 1000);
            context.setDefaultNavigationTimeout(60 * 1000);
            page = context.newPage();
            page.setDefaultTimeout(20 * 1000);
            page.setDefaultNavigationTimeout(20 * 1000);
            apiRequestContext = playwright.request().newContext(new APIRequest.NewContextOptions());

            listener = monitorPageRequest(apiRequestContext, page);

            // 开启trace录制，可以通过trace文件，帮助分析问题
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(false)
                    .setSnapshots(true)
                    .setSources(false)
            );

            login();

            // 如果开关开启，则不运行登录后的其他操作了，默认关闭
            if ("false".equalsIgnoreCase(loginSwitch)) {
                actions();

                logout();
            }


            // 执行成功，将结果写入库表，成功了不记库表了
            // insertRecord("success", "na");

            jobResult = ReturnT.SUCCESS;
            return jobResult;
        } catch (Exception e) {
            jobResult = ReturnT.FAIL;
            jobResult.setMsg(e.getMessage());

            // 执行失败，截个图的，可以通过命令将文件复制出容器查看：curl -F 'x=@/tmp/ui-monitor/20230321132133.png' file.pingpongx.com/disk
            // 打开地址：https://file.pingpongx.com/disk
            // if (page != null) {
            //    page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("/tmp/ui-monitor/" + getFormattedTime() + ".png")));
            // }
            log.warn("monitor, message size: " + e.getMessage());
            // 如果是登录失败，才发送钉钉告警
            if (apiRequestContext != null && e instanceof LoginException) {
                // sendWarnMessage(apiRequestContext, e.getMessage());
                log.error("{}监控：{}", this.business, e.getMessage());
            }

            // 执行失败，写入库表
            insertRecord("failed", e.getMessage());
            return jobResult;
        } finally {
            // 停止trace录制
            if (context != null) {
                context.tracing().stop(new Tracing.StopOptions()
                        .setPath(Paths.get(localResultPath.toString() +
                                "/" + business + "-trace-" + TimeUtils.getFormattedTime() +".zip")));
            }
            if (page != null && listener != null) {
                page.offResponse(listener);
            }
            Page.CloseOptions closeOptions = new Page.CloseOptions();
            closeOptions.setRunBeforeUnload(true);
            // 不能直接关闭，可能退出登录了，还有请求在进行中，会导致 page.onResponse监控的接口数据 报错
            if (page != null) {
                page.close(closeOptions);
                page = null;
            }
            // har文件是在context关闭后才生成的
            if (context != null) {
                context.close();
                context = null;
            }
            // 如果执行成功了，则删除录制的视频及目录
            if (jobResult != null && jobResult.getCode() == ReturnT.SUCCESS.getCode()) {
                clearLocalResult(localResultPath);
            } else if (jobResult != null && jobResult.getCode() == ReturnT.FAIL.getCode()) {
                // 上传文件到文件服务器并发送钉钉告警
                if (apiRequestContext != null) {
                    uploadUiMonitorFiles(apiRequestContext, localResultPath);
                    String failReason = jobResult.getMsg();
                    String formattedFailReason = extractStringByReg(failReason, "logs =+(.*?)=").trim();
                    log.info("formattedFailReason:{}", formattedFailReason);
                    sendUIMonitorResultMsg(host, business, phoneNumber, jobStartTime,
                            "https://file.pingpongx.com/disk/"+localResultPath.toString().substring(16),
                            "".equals(formattedFailReason) ? failReason : formattedFailReason);
                }
            }
            if (apiRequestContext != null) {
                apiRequestContext.dispose();
            }
            if (browser != null) {
                browser.close();
                browser = null;
            }
            if (playwright != null) {
                playwright.close();
            }
        }
    }

    private void uploadUiMonitorFiles(APIRequestContext apiRequestContext, Path localResultPath) {
        // 创建文件夹，使用localResultPath时，截取文件夹的名字
        apiRequestContext.post("https://file.pingpongx.com/disk", RequestOptions.create().setForm(
                FormData.create().set("action", "folder").set("name", localResultPath.toString().substring(16))
        ));
        // 上传文件
        try {
            Files.walkFileTree(localResultPath, new SimpleFileVisitor<Path>() {
                //遍历上传文件
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    apiRequestContext.post("https://file.pingpongx.com/disk/"+localResultPath.toString().substring(16), RequestOptions.create().setMultipart(
                            FormData.create().set("fileField", file))
                    );
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用开发的接口发送钉钉告警，只会判断是生产环境才发告警
     * @param message
     */
    public void sendWarnMessage(APIRequestContext apiRequestContext, String message) {
        Map<String, String> data = new HashMap<>();
        data.put("appName", "smb-warning");
        data.put("className", this.getClass().getSimpleName());
        data.put("content", message);
        data.put("hostName", host);
        data.put("time", getFormattedTime());

        // 如果当前是生产环境，发告警出来
        if (monitorEnvParam.getMonitorEnv().equals(MonitorEnv.PROD.getMonitorEnv())) {
            log.info("error happened and data is:{}", JSON.toJSONString(data));
            // 在容器中执行下面的post方法时，容器中没有https，应访问http
            APIResponse postDingMsgRes = apiRequestContext.post("http://smb-warning.pingpongx.com/v2/alert/" + dingGroup, RequestOptions.create().setData(data));
            log.info("调用钉钉告警结果：{}", postDingMsgRes.text());
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
                if (response.request().url().contains(host) && !response.request().url().endsWith("png") && response.request().url().contains("api")) {
                    String resText = response.text();
                    if (StringUtils.hasLength(resText)) {
                        int code = JSONPath.read(resText, "$.code", Integer.class);
                        // todo 如果响应信息的内容长度过长需要告警吗？例如有一个接口的响应内容超过64K，目前超64K插入库表会报错的
                        // saveResponseDetail(response, code);
                        // 后续可能需要考虑code的判断条件，比如如果服务端错误，是5开头这种，
                        // 20404 40401 主站的code 码
                        if (code != 0 && code != 20404 && code != 40401) {
                            log.info("monitorPageRequest error, url:{}, code:{}", response.request().url(), code);
                            saveResponseDetail(response, code);
                            // 发送告警，50004是服务端超时错误，暂时不发告警
                            if (code != 50004) {
                                // sendWarnMessage(apiRequestContext,"api monitor error\nurl:"+ response.request().url() + "\n,res:" + resText);
                                log.error("{}监控, url:{}, res:{}", this.business, response.request().url(), resText);
                                sendApiMonitorResultMsg(business, phoneNumber, response.request().url(), getFormattedTime(), resText);
                            }
                            log.warn("api monitor error url:{}, code: {}, res:{} ", response.request().url(), code, resText);
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

        // 对页面上可能的弹窗进行处理，例如温馨提示什么的
//        Consumer<Page> pageConsumer = pageC -> {
//            if (waitElementExist(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Close")), 400)) {
//                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Close")).click();
//            }
//        };
//        page.onPopup(pageConsumer);
//
//        Consumer<Dialog> dialogConsumer = dialog -> {
//            log.debug("dialog message:{}", dialog.message());
//        };
//        page.onDialog(dialogConsumer);

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
//            if (route.request().url().contains("/api/front/v2/auth/token")) {
//                Random random = new Random();
//                StringBuilder sb = new StringBuilder("47.96.196.");
//                sb.append(random.nextInt(255));
//                log.info("fm login forwarded ip: {}", sb.toString());
//                headers.put("X-Forwarded-For", sb.toString());
//            }
//            route.resume(new Route.ResumeOptions().setHeaders(headers));
//        });

        // 如果是福贸的生产环境，需要增加特定的key，以避免被限流拦截
        if ("https://flowmore.pingpongx.com".equals(host)) {
            page.route("**/*", route -> {
                // Override headers
                Map<String, String> headers = new HashMap<>(route.request().headers());
                if (route.request().url().contains("/api/front/v2/auth/token")) {
                    // 此配置是在 ppconfig中 business-gateway下面，
                    headers.put("monitor", "monitorTest");
                }
                route.resume(new Route.ResumeOptions().setHeaders(headers));
            });
        }

        return listener;
    }

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("param is: {}",param);

        initUtil();

        return monitor();
    }
}
