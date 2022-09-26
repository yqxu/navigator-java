package com.pingpongx.com.smb.warning.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pingpongx.smb.warning.dependency.client.DingTalkRobotsClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class DingTalkTest extends BaseServerTest {

    @Autowired
    private DingTalkRobotsClient dingTalkRobotsClient;

    @Test
    @SneakyThrows
    public void test_get_token() {
        dingTalkRobotsClient.getToken();
    }


    @Test
    @SneakyThrows
    public void test_send_group_chat() {
        String conversationId = "cidCrgIRWMxDT5HL20aXDw1Kw==";
        String msgKey = "sampleText";
        String answer = "cc：" + System.currentTimeMillis();
        String msgParam = "{\"content\":\"" + answer + "\"}";
        dingTalkRobotsClient.groupChat(conversationId, msgParam, msgKey);
    }

    @Test
    @SneakyThrows
    public void test_send_single_chat() {
        String senderStaffId = "051908373921722813";
        String msgKey = "sampleText";
        String answer = "cc：" + System.currentTimeMillis();
        String msgParam = "{\"content\":\"" + answer + "\"}";
        dingTalkRobotsClient.singleChat(senderStaffId, msgParam, msgKey);
    }
}
