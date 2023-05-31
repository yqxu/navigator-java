package com.pingpongx.smb.monitor.biz.util;

import com.alibaba.fastjson.JSON;
import com.pingpongx.smb.warning.biz.util.HttpAPI;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

/**
 * ui监控失败，发消息到指定的群里
 */
@Slf4j
public class DingUtils {

    private static final String webHook = "https://oapi.dingtalk.com/robot/send?access_token=dc47ee0bd70d5074b2eec4baa46c8265a4dad30b3bea76b6fe005bed9019340f";
    private static final String secret = "SECa4e69bec2263e2dac5a4d7d34f967429156a6a12b61f2ab618442c752a4bb9ee";

    private static String getSign(Long timestamp) {
        String stringToSign = timestamp + "\n" + secret;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return URLEncoder.encode(new String(Base64.encodeBase64(signData)),StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void send(String url, String msg) {
        try {
            new HttpAPI().postJson(url, new HashMap<>(), msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendUIMonitorResultMsg(String host, String businessLine, List<String> phoneNumberList, String jobStartTime, String folderUrl, int continueFailedTimes, String failReason)  {
        Long timestamp = System.currentTimeMillis();
        String sign = getSign(timestamp);

        MarkDownMsg msg = new MarkDownMsg();
        msg.setMsgtype("markdown");
        MarkDown markDown = new MarkDown();
        markDown.setTitle("UI监控告警");
        StringBuilder sb = new StringBuilder();
        sb.append("### " + businessLine);
        for (String phone : phoneNumberList) {
            sb.append(" @" + phone);
        }
        sb.append("\n");
        sb.append(
                "### <font color='red'>执行失败，请检查</font>\n" +
                "##### 执行环境：" + host + "\n" +
                "##### 失败原因：" + failReason + "\n" +
                "##### 任务开始时间：" + jobStartTime + "\n" +
                "##### 连续失败：" + continueFailedTimes + "\n" +
                "##### 视频及trace文件：[戳我](" + folderUrl+")");

        markDown.setText(sb.toString());
        msg.setMarkdown(markDown);
        At at = new At();
        at.setIsAtAll(false);
        at.setAtMobiles(phoneNumberList);
        msg.setAt(at);
        log.info("sendUIMonitorResultMsg msg:{}", JSON.toJSONString(msg));

        send(webHook + "&timestamp=" + timestamp + "&sign=" + sign, JSON.toJSONString(msg));
    }


    public static void sendApiMonitorResultMsg(String businessLine, List<String> phoneNumberList, String apiUrl, String httpStatus, String apiExecTime, String apiRes) {
        Long timestamp = System.currentTimeMillis();
        String sign = getSign(timestamp);

        MarkDownMsg msg = new MarkDownMsg();
        msg.setMsgtype("markdown");
        MarkDown markDown = new MarkDown();
        markDown.setTitle("接口调用告警");
        StringBuilder sb = new StringBuilder("### " + businessLine);
        for (String phone : phoneNumberList) {
            sb.append(" @" + phone);
        }
        sb.append("\n");
        sb.append(
                "### <font color='red'>接口调用出错，请检查</font>\n" +
                "##### 接口：" + apiUrl + "\n" +
                "##### 响应码：" + httpStatus + "\n" +
                "##### 时间：" + apiExecTime + "\n" +
                "##### 响应：" + apiRes);
        markDown.setText(sb.toString());
        msg.setMarkdown(markDown);
        At at = new At();
        at.setIsAtAll(false);
        at.setAtMobiles(phoneNumberList);
        msg.setAt(at);

        log.info("sendApiMonitorResultMsg msg:{}", JSON.toJSONString(msg));

        send(webHook + "&timestamp=" + timestamp + "&sign=" + sign, JSON.toJSONString(msg));

    }



    public static void main(String[] args) {
//        String s = "Error {\n" +
//                "  message='Timeout 60000ms exceeded.\n" +
//                "=========================== logs ===========================\n" +
//                "waiting for getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(\"付款人名称/收款账户\"))\n" +
//                "============================================================\n" +
//                "  name='TimeoutError\n" +
//                "  stack='TimeoutError: Timeout 60000ms exceeded.\n" +
//                "=========================== logs ===========================\n" +
//                "waiting for getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(\"付款人名称/收款账户\"))\n" +
//                "============================================================";
//
//        System.out.println(extractStringByReg(s, "logs =+(.*?)=").trim());
        //sendUIMonitorResultMsg("Flowmore", "15105163710", "https://file.pingpongx.com/disk/ui-monitor-20230510203219/0ab121e34a9ad07c6eca4a5976bd37b2.webm", "https://file.pingpongx.com/disk/ui-monitor-20230510203219");

//        sendApiMonitorResultMsg("Flowmore", "15105163710", "https://xx", "2024", "res");
    }

    @Data
    static class MarkDown {
        private String title;
        private String text;
    }

    @Data
    static class At {
        private List<String> atMobiles;
        private List<String> atUserIds;
        private Boolean isAtAll;

    }

    @Data
    static class MarkDownMsg {
        private String msgtype;
        private MarkDown markdown;
        private At at;

    }

}

