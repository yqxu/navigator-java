package com.pingpongx.smb.warning.dependency.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fm.commons.httpclient.HttpFluent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tangsh
 * @date 2022/09/26
 */
@Service
@Slf4j
public class DingTalkRobotsClient {

    @Value("${smb.dingtalk.robots.app.key:dingsawyscra8ocffp6d}")
    private String dingTalkAppKey;
    @Value("${smb.dingtalk.robots.app.key:xN6wdj73ZO5GfA7YHzY-x2YYikQpYk4Qev-rXrYdIbBa3ky-AffAznIF6hMXfP3B}")
    private String dingTalkAppSecret;
    @Value("${smb.dingtalk.robots.access.token.url:https://api.dingtalk.com/v1.0/oauth2/accessToken}")
    private String accessTokenUrl;
    @Value("${smb.dingtalk.robots.group.chat.url:https://api.dingtalk.com/v1.0/robot/groupMessages/send}")
    private String groupChatUrl;
    @Value("${smb.dingtalk.robots.group.chat.url:https://api.dingtalk.com/v1.0/robot/oToMessages/batchSend}")
    private String singleChatUrl;

    private static final String HEADER_ACCESS_TOKEN = "x-acs-dingtalk-access-token";

    @SneakyThrows
    public String getToken() {
        JSONObject params = new JSONObject();
        params.put("appKey", dingTalkAppKey);
        params.put("appSecret", dingTalkAppSecret);
        String content = HttpFluent.post(accessTokenUrl, params.toJSONString(), Lists.newArrayList());
        JSONObject respJson = JSON.parseObject(content);
        log.info("respJson is:{}", respJson.toJSONString());
        return respJson.getString("accessToken");
    }

    @SneakyThrows
    public void groupChat(String openConversationId, String msgParam, String msgKey) {
        JSONObject params = new JSONObject();
        params.put("robotCode", dingTalkAppKey);
        params.put("openConversationId", openConversationId);
        params.put("msgParam", msgParam);
        params.put("msgKey", msgKey);

        List<Header> headers = HttpFluent.addHeader(new BasicHeader(HEADER_ACCESS_TOKEN, getToken()));
        String content = HttpFluent.post(groupChatUrl, params.toJSONString(), headers);
        JSONObject respJson = JSON.parseObject(content);
        log.info("respJson is:{}", respJson.toJSONString());
    }


    @SneakyThrows
    public void singleChat(String userIds, String msgParam, String msgKey) {
        JSONObject params = new JSONObject();
        params.put("robotCode", dingTalkAppKey);
        params.put("userIds", Lists.newArrayList(userIds));
        params.put("msgParam", msgParam);
        params.put("msgKey", msgKey);

        List<Header> headers = HttpFluent.addHeader(new BasicHeader(HEADER_ACCESS_TOKEN, getToken()));
        String content = HttpFluent.post(singleChatUrl, params.toJSONString(), headers);
        JSONObject respJson = JSON.parseObject(content);
        log.info("respJson is:{}", respJson.toJSONString());
    }



}
