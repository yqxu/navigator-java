package com.pingpongx.smb.monitor.biz.util;

import com.alibaba.fastjson.JSON;
import com.pingpongx.smb.warning.biz.util.HttpAPI;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
public class HikerResultUploadUtils {

    private static void send(String url, String msg) {
        try {
            new HttpAPI().postJson(url, new HashMap<>(), msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void uploadUiMonitorResult(E2EUIRunRecordDTO dto) {
        send("https://test-patrol-service.pingpongx.com/patrol-service/api/e2e-ui/record/insert", JSON.toJSONString(dto));
        log.info("uploadUiMonitorResult finished");
    }

    public static void uploadApiMonitorResult(E2EAPIRunRecordDTO dto) {
        send("https://test-patrol-service.pingpongx.com/patrol-service/api/e2e-api/record/insert", JSON.toJSONString(dto));
        log.info("uploadApiMonitorResult finished");
    }

    @Data
    public static class E2EUIRunRecordDTO {
        private String businessLine;

        private String env;

        //0成功、-1失败
        private Integer result;

        private String failCause;

        private String videoLink;

        private String traceLink;
    }

    @Data
    public static class E2EAPIRunRecordDTO {
        private String businessLine;

        private String env;

        //0成功、-1失败
        private Integer result;

        private String apiUrl;

        private Integer httpCode;

        private Integer apiCode;

        private String apiRespContent;
    }
}
