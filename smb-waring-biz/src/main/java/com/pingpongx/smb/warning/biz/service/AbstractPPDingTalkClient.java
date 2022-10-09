package com.pingpongx.smb.warning.biz.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.request.OapiRobotSendRequest.Markdown;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.google.common.collect.Lists;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/07/10:47 上午
 * @Description:
 * @Version:
 */
@Slf4j
public abstract class AbstractPPDingTalkClient implements PPDingTalkClient {

    abstract public String getNotifyUrl();
    private final DingTalkClient dingTalkClient;
    private final String notifyUrl;

    public AbstractPPDingTalkClient() {
        this.notifyUrl = getNotifyUrl();
        this.dingTalkClient = new DefaultDingTalkClient(getNotifyUrl());
    }

    public void sendMarkDown(String title,String content, List<String> notifyDingUserList){
        OapiRobotSendRequest robotRequest = new OapiRobotSendRequest();
        robotRequest.setMsgtype("markdown");
        Markdown markdown =  new Markdown();
        markdown.setTitle(title);
        markdown.setText(content);
        robotRequest.setMarkdown(markdown);
        robotRequest.setAt(atUser(notifyDingUserList));
        this.doSend(robotRequest);
    }

    private OapiRobotSendRequest.At atUser(List<String> notifyDingUserList){
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(CollectionUtils.isEmpty(notifyDingUserList)? Lists.newArrayList(""):notifyDingUserList);
        at.setIsAtAll(false);
        return at;
    }

    private void doSend(OapiRobotSendRequest robotRequest){
        try {
            OapiRobotSendResponse execute = dingTalkClient.execute(robotRequest);
            if (execute.isSuccess()){
                log.info("DingTalkService.sendDingTalk[发送钉钉消息成功]");
            }else {
                log.error("DingTalkService.sendDingTalk[发送钉钉消息失败],errorCode:{},errorMsg:{}",execute.getCode(),execute.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("DingTalkService.sendDingTalk[发送钉钉消息失败]",e);
        }
    }

    public void sendDingTalkText(String content, List<String> notifyDingUserList){
        OapiRobotSendRequest robotRequest = new OapiRobotSendRequest();
        log.info("DingTalkClient.sendDingTalk[发送钉钉消息:content:[{}],notifyUrl:{},通知人信息:{}]",content,notifyUrl,notifyDingUserList);
        robotRequest.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        robotRequest.setText(text);
        robotRequest.setAt(atUser(notifyDingUserList));
        this.doSend(robotRequest);
    }
}
