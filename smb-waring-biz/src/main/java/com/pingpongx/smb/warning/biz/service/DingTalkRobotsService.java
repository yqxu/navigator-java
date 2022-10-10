package com.pingpongx.smb.warning.biz.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pingpongx.smb.warning.biz.enums.ConversationTypeEnum;
import com.pingpongx.smb.warning.dal.dataobject.SmbQaLibrary;
import com.pingpongx.smb.warning.dal.dataobject.SmbQuestionRecord;
import com.pingpongx.smb.warning.dal.repository.SmbQaLibraryRepository;
import com.pingpongx.smb.warning.dal.repository.SmbQuestionRecordRepository;
import com.pingpongx.smb.warning.dependency.client.DingTalkRobotsClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
    private SmbQuestionRecordRepository questionRecordRepository;
    @Autowired
    private DingTalkRobotsClient robotsClient;

    public void responseDingTalk(JSONObject json) {
        if (json == null) {
            log.warn("钉钉发送消息内容为空!");
            return;
        }
        SmbQuestionRecord record = saveQuestion(json);
        String content = record.getQuestion();
        String conversationType = record.getConversationType();
        log.info("请求问题:{}", content);
        //eg. 信用卡 手续费，则优先查询 信用卡类目下的相关问题.
        String[] contentArray = content.split(StringUtils.SPACE);
        List<String> contentWords = Lists.newArrayList();
        Arrays.stream(contentArray).filter(StringUtils::isNotBlank).forEach(contentWords::add);
        if (CollectionUtils.isEmpty(contentWords)) {
            return;
        }
        log.info("问题集:{}", JSON.toJSONString(contentWords));
        String category = contentWords.get(0);
        String keywords = StringUtils.EMPTY;
        if (contentWords.size() >= 2) {
            keywords = contentWords.get(contentWords.size() - 1);
        }
        SmbQaLibrary qaLibrary = null;
        List<SmbQaLibrary> qaLibraryList = qaLibraryRepository.queryByCategory(category);
        //1.如果类目命中，且全关键词也命中。
        if (CollectionUtils.isNotEmpty(qaLibraryList)) {
            if (StringUtils.isNotEmpty(keywords)) {
                qaLibrary = qaLibraryRepository.getOneQa(keywords);
            } else {
                //2.只有关键词。随机返回问题
                int count = qaLibraryList.size();
                Random r = new Random();
                int randomNum = r.nextInt(count);
                qaLibrary = qaLibraryList.get(randomNum);
            }
        } else {
            //3.如果类目没命中，但命中了关键词，则返回
            if (StringUtils.isNotEmpty(keywords)) {
                qaLibrary = qaLibraryRepository.getOneQa(keywords);
            }
        }
        responseDingTalk(json, conversationType, qaLibrary);
    }

    private void responseDingTalk(JSONObject json, String conversationType, SmbQaLibrary qaLibrary) {
        String msgParam;
        String msgType;
        ConversationTypeEnum typeEnum = ConversationTypeEnum.getByCode(conversationType);
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


    private SmbQuestionRecord saveQuestion(JSONObject json) {
        String senderNick = json.getString("senderNick");
        String conversationId = json.getString("conversationId");
        String conversationType = json.getString("conversationType");
        ConversationTypeEnum typeEnum = ConversationTypeEnum.getByValue(conversationType);
        String msgType = json.getString("msgtype");
        String userId = json.getString("senderStaffId");
        String content = json.getJSONObject("text").getString("content").trim();
        SmbQuestionRecord record = new SmbQuestionRecord();
        record.setUserName(senderNick);
        record.setUserId(userId);
        record.setQuestion(content);
        record.setMsgType(msgType);
        record.setConversationId(conversationId);
        record.setConversationType(typeEnum.getCode());
        questionRecordRepository.save(record);
        return record;
    }
}
