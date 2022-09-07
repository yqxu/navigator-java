package com.pingpongx.smb.warning.biz.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.request.OapiRobotSendRequest.Markdown;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.google.common.collect.Lists;
import com.taobao.api.ApiException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/07/10:47 上午
 * @Description:
 * @Version:
 */
@Slf4j
public class DingTalkService {
    private final DingTalkClient dingTalkClient;

    private final OapiRobotSendRequest robotRequest;

    private final String notifyUrl;

    public DingTalkService(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        this.dingTalkClient = new DefaultDingTalkClient(notifyUrl);
        this.robotRequest = new OapiRobotSendRequest();
    }

    public void sendDingTalkMarkDown(String title,String content, List<String> notifyDingUserList){
        robotRequest.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown =  new Markdown();
        markdown.setTitle(title);
        markdown.setText(content);
        robotRequest.setMarkdown(markdown);
        robotRequest.setAt(findAtUser(notifyDingUserList));
        this.execute();
    }

    private OapiRobotSendRequest.At findAtUser(List<String> notifyDingUserList){
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(CollectionUtils.isEmpty(notifyDingUserList)? Lists.newArrayList(""):notifyDingUserList);
        at.setIsAtAll(false);
        return at;
    }

    private void execute(){
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
        log.info("DingTalkClient.sendDingTalk[发送钉钉消息:content:[{}],notifyUrl:{},通知人信息:{}]",content,notifyUrl,notifyDingUserList);
        robotRequest.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        robotRequest.setText(text);
        robotRequest.setAt(findAtUser(notifyDingUserList));
        this.execute();
    }
}
