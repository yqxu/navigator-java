package com.pingpongx.smb.warning.biz.enums;

import com.pingpongx.business.common.exception.BizException;
import com.pingpongx.business.common.exception.ErrorCode;
import lombok.Getter;

/**
 * 会话消息类型
 *
 * @author tangsihang
 * @date 2022/9/26
 */
@Getter
public enum ConversationTypeEnum {

    /**
     * 会话消息类型
     */

    SINGLE_CHAT("SINGLE_CHAT", "1"),

    GROUP_CHAT("GROUP_CHAT", "2");

    private String code;
    private String value;


    ConversationTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static ConversationTypeEnum getByValue(String value) {
        for (ConversationTypeEnum statusEnum : values()) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum;
            }
        }
        throw new BizException(ErrorCode.BIZ_BREAK, "钉钉会话消息类型异常");
    }
}
