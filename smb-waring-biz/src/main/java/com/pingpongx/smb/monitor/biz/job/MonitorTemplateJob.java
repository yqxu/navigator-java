package com.pingpongx.smb.monitor.biz.job;

import com.alibaba.fastjson.JSONPath;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import com.pingpongx.job.core.biz.model.ReturnT;
import com.pingpongx.job.core.handler.IJobHandler;
import com.pingpongx.smb.monitor.biz.util.HikerResultUploadUtils;
import com.pingpongx.smb.monitor.biz.util.TimeUtils;
import com.pingpongx.smb.monitor.dal.entity.constant.BusinessLine;
import com.pingpongx.smb.monitor.dal.entity.dataobj.ApiDetail;
import com.pingpongx.smb.monitor.dal.entity.dataobj.TaskRecord;
import com.pingpongx.smb.monitor.dal.entity.uiprops.MonitorEnvParam;
import com.pingpongx.smb.monitor.dal.mapper.ApiDetailMapper;
import com.pingpongx.smb.monitor.dal.mapper.TaskRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.pingpongx.smb.monitor.biz.util.DingUtils.sendApiMonitorResultMsg;
import static com.pingpongx.smb.monitor.biz.util.DingUtils.sendUIMonitorResultMsg;
import static com.pingpongx.smb.monitor.biz.util.HikerResultUploadUtils.uploadApiMonitorResult;
import static com.pingpongx.smb.monitor.biz.util.HikerResultUploadUtils.uploadUiMonitorResult;
import static com.pingpongx.smb.monitor.biz.util.PlayWrightUtils.initUtil;
import static com.pingpongx.smb.monitor.biz.util.RegUtils.extractStringByReg;
import static com.pingpongx.smb.monitor.biz.util.TimeUtils.getFormattedTime;
import static com.pingpongx.smb.monitor.biz.util.TimeUtils.getFormattedTime2;

/**
 * 问题：
 * 1. 主站监控的是ro环境，不是生产
 * 2. b2b有时候打开登录页，是空白页面，刷不出登录页
 * 3. 福贸登录时，有时会触发类似ddos拦截，导致响应信息拿不到数据 => 在header中加入指定的key来跳过拦截
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
    private String business;
    private Page page;
    private List<String> phoneNumberList;
    private String loginSwitch;

    // 不同的job执行时，使用的是不同的线程号，理论上是线程安全的
    private int continueFailedTimes = 0;

    public void setLoginSwitch(String loginSwitch) {
        this.loginSwitch = loginSwitch;
    }

    public void setPhoneNumberList(List<String> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
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
        String jobStartTime = getFormattedTime2();
        log.info("开始时间：{}，当前monitor的环境：{}，当前业务线：{}", jobStartTime, monitorEnvParam.getMonitorEnv(), business);
        Playwright playwright = null;
        Consumer<Response> listener = null;
        Browser browser = null;
        BrowserContext context = null;
        APIRequestContext apiRequestContext = null;
        Path localResultPath = Paths.get("/tmp/ui-monitor/ui-monitor-" + monitorEnvParam.getMonitorEnv() + "-" + business + "-" + getFormattedTime());
        ReturnT<String> jobResult = null;
        Browser.NewContextOptions newContextOptions = new Browser.NewContextOptions()
                // 因为福贸的业务有向导弹窗，弹出来比较恶心，而且是前端记忆的，所以在启动浏览器时把这个填入localStorage里面
                .setStorageState("{\"origins\":[{\"origin\":\"" + host + "\",\"localStorage\":[{\"name\":\"guideStep\",\"value\":\"{\\\"haveKyc\\\":true,\\\"vaGuide\\\":true,\\\"vaUseGuide\\\":true,\\\"firstInbound\\\":false,\\\"inboundGuide1\\\":false,\\\"inboundGuide2\\\":false,\\\"inboundGuide3\\\":false}\"},{\"name\":\"LockExchangeGuide\",\"value\":\"1\"},{\"name\":\"menuV2Tips\",\"value\":\"1\"}]}]}")
                .setViewportSize(1440, 874)
                .setRecordVideoSize(1440, 874)
                .setRecordVideoDir(localResultPath)
                .setRecordHarPath(Paths.get(localResultPath.toString() + "/harFile.har"))
                .setRecordHarMode(HarMode.MINIMAL)
                ;
        // 如果是主站的，只录主站的登录，如果是福贸的，只录福贸的登录，如果是b2b的业务，全录
        if ("flowmore".equals(business)) {
            newContextOptions.setRecordHarUrlFilter(host + "/api/front/v2/auth/token");
        } else if ("merchant".equals(business)) {
            newContextOptions.setRecordHarUrlFilter(host + "/api/user/web/login");
        }

        try {
            playwright = Playwright.create();
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                    .setHandleSIGHUP(true)
                    .setHandleSIGINT(true)
                    .setHandleSIGTERM(true)
                    .setHeadless(true)
                    .setDevtools(false)
                    .setSlowMo(2200);
            // 如果是b2b的业务，给配个代理
            if ("b2b".equals(business)) {
                Proxy proxy = new Proxy("proxy-us.pingpongx.com:3128");
                launchOptions.setProxy(proxy);
            }
            browser = playwright.chromium().launch(launchOptions);
            // 不同的context的配置，理论上是一样的，例如浏览器的尺寸
            context = browser.newContext(newContextOptions);
            context.setDefaultTimeout(30 * 1000);
            context.setDefaultNavigationTimeout(30 * 1000);
            page = context.newPage();
            page.setDefaultTimeout(30 * 1000);
            page.setDefaultNavigationTimeout(30 * 1000);
            apiRequestContext = playwright.request().newContext(new APIRequest.NewContextOptions());

            listener = monitorPageRequest(apiRequestContext, page, localResultPath);

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

            // 执行失败，写入库表
            insertRecord("failed", e.getMessage());
            return jobResult;
        } finally {
            // 停止trace录制
            if (context != null) {
                context.tracing().stop(new Tracing.StopOptions()
                        .setPath(Paths.get(localResultPath.toString() +
                                "/" + business + "-trace-" + TimeUtils.getFormattedTime() +".zip")));
                saveBrowserStorageInfo(localResultPath, context);
            }
            if (page != null && listener != null) {
                page.offResponse(listener);
            }
            Page.CloseOptions closeOptions = new Page.CloseOptions();
            closeOptions.setRunBeforeUnload(true);
            // 不能直接关闭，可能退出登录了，还有请求在进行中，会导致 page.onResponse监控的接口数据 报错
            if (page != null) {
                page.close(closeOptions);
            }
            // har文件是在context关闭后才生成的
            if (context != null) {
                log.info("context pages: {}", context.pages().size());
                context.close();
            }
            if (jobResult != null) {
                // 如果执行成功了，则删除录制的视频及目录
                if (jobResult.getCode() == ReturnT.SUCCESS.getCode()) {
                    clearLocalResult(localResultPath);
                    continueFailedTimes = 0;
                    uploadUIResultToHiker(0, "", "", "");
                } else {
                    continueFailedTimes++;
                    // 上传文件到文件服务器并发送钉钉告警
                    if (apiRequestContext != null) {
                        uploadUiMonitorFiles(apiRequestContext, localResultPath);
                        String formattedFailReason = extractStringByReg(jobResult.getMsg(), "logs =+(.*?)=").trim();
                        String failReason = "".equals(formattedFailReason) ? jobResult.getMsg() : formattedFailReason;
                        log.info("formattedFailReason:{}", formattedFailReason);

                        sendUIMonitorResultMsg(host, business, phoneNumberList, jobStartTime,
                                "https://file.pingpongx.com/disk/" + localResultPath.toString().substring(16),
                                continueFailedTimes, failReason);

                        uploadUIResultToHiker(-1, failReason,
                                "https://file.pingpongx.com/disk/" + localResultPath.toString().substring(16),
                                "https://file.pingpongx.com/disk/" + localResultPath.toString().substring(16));
                    }
                }
            }
            if (apiRequestContext != null) {
                apiRequestContext.dispose();
            }
            if (browser != null) {
                browser.close();
            }
            if (playwright != null) {
                playwright.close();
            }
        }
    }

    private void uploadUIResultToHiker(int result, String failReason, String traceLink, String videoLink) {
        HikerResultUploadUtils.E2EUIRunRecordDTO dto = new HikerResultUploadUtils.E2EUIRunRecordDTO();
        dto.setBusinessLine(business);
        dto.setEnv(monitorEnvParam.getMonitorEnv());
        dto.setResult(result);
        dto.setFailCause(failReason);
        dto.setTraceLink(traceLink);
        dto.setVideoLink(videoLink);
        uploadUiMonitorResult(dto);
    }

    private void uploadAPIResultToHiker(Integer code, String url, String res, String httpStatus) {
        HikerResultUploadUtils.E2EAPIRunRecordDTO dto = new HikerResultUploadUtils.E2EAPIRunRecordDTO();
        dto.setApiCode(code);
        dto.setApiUrl(url);
        dto.setApiRespContent(res);
        dto.setBusinessLine(business);
        try {
            dto.setHttpCode(Integer.parseInt(httpStatus));
        } catch (Exception e) {
            dto.setHttpCode(-1);
        }
        dto.setResult(-1);
        dto.setEnv(monitorEnvParam.getMonitorEnv());
        uploadApiMonitorResult(dto);
    }

    /**
     * 保存浏览器的Cookie，LocalStorage信息到文件
     * @param localResultPath
     * @param context
     */
    private void saveBrowserStorageInfo(Path localResultPath, BrowserContext context) {
        try {
            File browserStorageInfo = new File(localResultPath.toString() + "/BrowserStorageInfo.json");
            FileUtils.write(browserStorageInfo, context.storageState(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
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
                    apiRequestContext.post("https://file.pingpongx.com/disk/" + localResultPath.toString().substring(16), RequestOptions.create().setMultipart(
                            FormData.create().set("fileField", file))
                    );
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
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
    private Consumer<Response> monitorPageRequest(APIRequestContext apiRequestContext, Page page, Path localResultPath) {
        Consumer<Response> listener = response -> {
            String httpStatus = "";
            try {
                //todo 对响应的header判断，如果返回的类型是html，一种处理方式，如果返回的类型是json再以json处理？
                if (response.request().url().contains(host) && !response.request().url().endsWith("png") && response.request().url().contains("api")) {
                    try {
                        httpStatus = "" + response.status();
                    } catch (Exception ex1) {
                        httpStatus = response.statusText();
                    }
                    String resText = response.text();
                    if (StringUtils.hasLength(resText)) {
                        int code;
                        try {
                            code = JSONPath.read(resText, "$.code", Integer.class);
                        } catch (Exception ex2) {
                            throw new RuntimeException("响应结果不是合理的json串，Content-Type:" + response.headerValue("Content-Type"));
                        }
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
                                // log.error("{}监控, url:{}, res:{}", this.business, response.request().url(), resText);
                                sendApiMonitorResultMsg(business, phoneNumberList, response.request().url(), httpStatus, getFormattedTime2(), resText);
                                uploadAPIResultToHiker(code, response.url(), resText, httpStatus);
                            }
                            log.warn("api monitor error url:{}, code: {}, res:{} ", response.request().url(), code, resText);
                        }
                    }
                }
            } catch (Throwable throwable) {
                // 可能会因为服务端没有给响应信息而中断，或者因为服务端ng出错，导致接口报502等ng出错的html给到接口出来，导致解析json出错
                if (throwable.getMessage() == null) {
                    log.warn("throwable.getMessage() is null");
                    sendApiMonitorResultMsg(business, phoneNumberList, response.request().url(), httpStatus, getFormattedTime2(), "throwable.getMessage() is null");
                }
                if (throwable.getMessage() != null && !throwable.getMessage().contains("No resource with given identifier found")) {
                    log.warn("monitor url: {}, error:{}", response.request().url(), throwable.getMessage());
                    sendApiMonitorResultMsg(business, phoneNumberList, response.request().url(), httpStatus, getFormattedTime2(), throwable.getMessage());
                }
            }
        };
        page.onResponse(listener);

        File consoleLogMsgFile = new File(localResultPath.toString() + "/ConsoleLogMsg.txt");
        page.onConsoleMessage(msg -> {
            log.info("console msg, arg:{}, text:{}", msg, msg.text());
            // 保存控制台输出到文件
            try {
                FileUtils.write(consoleLogMsgFile, msg.text() + "\n", StandardCharsets.UTF_8, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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

        // 解决部分 b2b站点访问常失败的问题
        if ("b2b".equals(business)) {
            page.route("**/*", route -> {
                if (route.request().url().contains("q.quora.com") ||
                        route.request().url().contains("rp.gif") ||
                        route.request().url().contains("fbevents.js"))
                    route.abort();
                else
                    route.resume();
            });
        }


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
    public ReturnT<String> execute(String param) {
        log.info("param is: {}",param);

        // 调用静态类的方法，对静态类初始化
        initUtil();

        return monitor();
    }
}
