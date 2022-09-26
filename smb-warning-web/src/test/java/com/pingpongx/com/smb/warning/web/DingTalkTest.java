package com.pingpongx.com.smb.warning.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fm.commons.httpclient.HttpFluent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;

@Slf4j
public class DingTalkTest extends BaseServerTest {


    private static final String dingTalkAppKey = "dingsawyscra8ocffp6d";
    private static final String dingTalkAppSecret = "xN6wdj73ZO5GfA7YHzY-x2YYikQpYk4Qev-rXrYdIbBa3ky-AffAznIF6hMXfP3B";

    @Test
    @SneakyThrows
    public void test_get_token() {
        getToken();
    }

    @Test
    @SneakyThrows
    public void test_get_user_id() {
        String getUserIdUrl = "https://oapi.dingtalk.com/topapi/v2/user/getbymobile?access_token=%s";
        String url = String.format(getUserIdUrl, getToken());
        JSONObject bodys = new JSONObject();
        bodys.put("support_exclusive_account_search", Boolean.FALSE);
        bodys.put("mobile", "15868124955");
        String content = HttpFluent.post(url, bodys.toJSONString(), Lists.newArrayList());
        log.info("{}", JSON.toJSONString(content));
        //"{\"errcode\":0,\"errmsg\":\"ok\",\"result\":{\"userid\":\"051908373921722813\"},\"request_id\":\"16m2kr7ov6r8t\"}"
    }

    @SneakyThrows
    public String getToken() {
        String getTokenUrl = "https://api.dingtalk.com/v1.0/oauth2/accessToken";
        JSONObject bodys = new JSONObject();
        bodys.put("appKey", dingTalkAppKey);
        bodys.put("appSecret", dingTalkAppSecret);
        String content = HttpFluent.post(getTokenUrl, bodys.toJSONString(), Lists.newArrayList());
        log.info("{}", JSON.toJSONString(content));
        JSONObject respJson = JSON.parseObject(content);
        String accessToken = respJson.getString("accessToken");
        log.info("access_token :{}", accessToken);
        return accessToken;
    }
}
