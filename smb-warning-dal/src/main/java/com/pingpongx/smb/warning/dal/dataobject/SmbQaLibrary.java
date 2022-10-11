package com.pingpongx.smb.warning.dal.dataobject;


import com.baomidou.mybatisplus.annotation.TableName;
import com.pingpongx.business.dal.core.BaseDO;
import lombok.Data;

/**
 * @author tangsh
 * @date 2022/09/26
 */
@Data
@TableName("smb_qa_library")
public class SmbQaLibrary extends BaseDO {

    private String bu;
    private String category;
    private String question;
    private String keyWord;
    private String answer;
    private String msgType;
}
