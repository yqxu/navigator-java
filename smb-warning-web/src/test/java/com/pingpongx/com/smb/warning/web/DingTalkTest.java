package com.pingpongx.com.smb.warning.web;

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
        String msgParam = "{\"content\":\"今天你发版了没\"}";
        dingTalkRobotsClient.groupTalk(conversationId, msgParam, msgKey);
    }

}
