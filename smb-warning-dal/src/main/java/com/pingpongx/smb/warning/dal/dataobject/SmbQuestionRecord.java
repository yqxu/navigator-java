package com.pingpongx.smb.warning.dal.dataobject;


import com.baomidou.mybatisplus.annotation.TableName;
import com.pingpongx.business.dal.core.BaseDO;
import lombok.Data;

/**
 * @author tangsh
 * @date 2022/09/26
 */
@Data
@TableName("smb_question_record")
public class SmbQuestionRecord extends BaseDO {

    private String userName;
    private String userId;
    private String question;
    private String msgType;
    private String conversationType;
    private String conversationId;
}
