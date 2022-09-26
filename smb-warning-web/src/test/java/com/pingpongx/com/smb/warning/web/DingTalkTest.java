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
    public void test_send_group_talk() {
        String conversationId = "cidCrgIRWMxDT5HL20aXDw1Kw==";
        String msgKey = "sampleText";
        String answer = "cc：" + System.currentTimeMillis();
        String msgParam = "{\"content\":\"" + answer + "\"}";
        dingTalkRobotsClient.groupTalk(conversationId, msgParam, msgKey);
    }


    @Test
    public void test_parse_content() {
        String msg = "{\n" +
                "\t\"conversationId\": \"cidCrgIRWMxDT5HL20aXDw1Kw==\",\n" +
                "\t\"atUsers\": [{\n" +
                "\t\t\"dingtalkId\": \"$:LWCP_v1:$6//0bcnwwn3xka880FNErl9OBIqLgebz\"\n" +
                "\t}],\n" +
                "\t\"chatbotCorpId\": \"ding0e8d089d86d0da35\",\n" +
                "\t\"chatbotUserId\": \"$:LWCP_v1:$6//0bcnwwn3xka880FNErl9OBIqLgebz\",\n" +
                "\t\"msgId\": \"msgI+zeGrUds7TgKkStlCQfBQ==\",\n" +
                "\t\"senderNick\": \"唐思航\",\n" +
                "\t\"isAdmin\": true,\n" +
                "\t\"senderStaffId\": \"051908373921722813\",\n" +
                "\t\"sessionWebhookExpiredTime\": 1664195786305,\n" +
                "\t\"createAt\": 1664190386147,\n" +
                "\t\"senderCorpId\": \"ding0e8d089d86d0da35\",\n" +
                "\t\"conversationType\": \"2\",\n" +
                "\t\"senderId\": \"$:LWCP_v1:$+8JCgkIp8fXdjLJEN8lOBg==\",\n" +
                "\t\"conversationTitle\": \"钉钉发送test\",\n" +
                "\t\"isInAtList\": true,\n" +
                "\t\"sessionWebhook\": \"https://oapi.dingtalk.com/robot/sendBySession?session=bd9662e8670b67a893a176403b4f055d\",\n" +
                "\t\"text\": {\n" +
                "\t\t\"content\": \" va\"\n" +
                "\t},\n" +
                "\t\"robotCode\": \"dingsawyscra8ocffp6d\",\n" +
                "\t\"msgtype\": \"text\"\n" +
                "}";
        JSONObject jsonObject = JSON.parseObject(msg);
        String content = jsonObject.getJSONObject("text").getString("content");
        log.info("{}", content);
        log.info("{}", content.trim());
    }

}
