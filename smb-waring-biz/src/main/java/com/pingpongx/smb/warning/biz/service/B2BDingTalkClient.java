package com.pingpongx.smb.warning.biz.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.request.OapiRobotSendRequest.Markdown;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.google.common.collect.Lists;
import com.pingpongx.smb.warning.biz.constant.Constant;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/07/10:47 上午
 * @Description:
 * @Version:
 */
@Slf4j
@Component
public class B2BDingTalkClient extends AbstractPPDingTalkClient implements PPDingTalkClient {
    public String getNotifyUrl(){
        return Constant.B2B.name;
    }
}
