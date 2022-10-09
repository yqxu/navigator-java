package com.pingpongx.smb.warning.biz.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.google.common.collect.Lists;
import com.taobao.api.ApiException;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public interface PPDingTalkClient {
    String getNotifyUrl();
    void sendMarkDown(String title,String content, List<String> notifyDingUserList);
    void sendDingTalkText(String content, List<String> notifyDingUserList);
}
