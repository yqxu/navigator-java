package com.pingpongx.smb.warning.biz.service;

import com.alibaba.fastjson.JSONObject;
import com.pingpongx.smb.warning.biz.enums.ConversationTypeEnum;
import com.pingpongx.smb.warning.dal.dataobject.SmbQaLibrary;
import com.pingpongx.smb.warning.dal.repository.SmbQaLibraryRepository;
import com.pingpongx.smb.warning.dependency.client.DingTalkRobotsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tangsh
 * @date 2022/09/26
 */

@Service
@Slf4j
public class DingTalkRobotsService {

    @Autowired
    private SmbQaLibraryRepository qaLibraryRepository;
    @Autowired
    private DingTalkRobotsClient robotsClient;

    public void responseDingTalk(JSONObject json) {
        //回复类型，1-单聊,2-群聊
        String conversationType = json.getString("conversationType");
        ConversationTypeEnum typeEnum = ConversationTypeEnum.getByValue(conversationType);
        String content = json.getJSONObject("text").getString("content").trim();
        log.info("请求问题:{}", content);
        SmbQaLibrary qaLibrary = qaLibraryRepository.getOneQa(content);
        String msgParam;
        String msgType;
        if (qaLibrary == null) {
            msgParam = "{\"content\":\"您问题的答案暂时不存在,可以联系管理员维护相关信息!\"}";
            msgType = "sampleText";
        } else {
            msgParam = qaLibrary.getAnswer();
            msgType = qaLibrary.getMsgType();
        }
        switch (typeEnum) {
            case SINGLE_CHAT:
                String senderStaffId = json.getString("senderStaffId");
                robotsClient.singleChat(senderStaffId, msgParam, msgType);
                break;
            case GROUP_CHAT:
                String conversationId = json.getString("conversationId");
                robotsClient.groupChat(conversationId, msgParam, msgType);
                break;
            default:
        }
        log.info("发送钉钉:{} 成功:{}", typeEnum, msgParam);
    }

}
