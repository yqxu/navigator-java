package com.pingpongx.smb.warning.biz.service;

import com.alibaba.fastjson.JSONObject;
import com.pingpongx.smb.warning.biz.enums.ConversationTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnsweringService {

    public void responseDingTalk(JSONObject json) {
        //回复类型，1-单聊,2-群聊
        String conversationType = json.getString("conversationType");
        ConversationTypeEnum typeEnum = ConversationTypeEnum.getByValue(conversationType);
        String conversationId = json.getString("conversationId");

        // 先做群聊模式
        switch (typeEnum) {
            case SING_TALK:

                break;
            case GROUP_TALK:


                break;
            default:
        }
        String content = json.getJSONObject("text").getString("content");
        String userId = json.get("senderStaffId").toString();
        log.info("{}", content);
        log.info("{}", userId);


    }



}
