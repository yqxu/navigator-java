package com.pingpongx.smb.warning.dal.dataobject;

import java.io.Serializable;
import lombok.Data;

/**
 * @Auther: jiangkun
 * @Date: 2022/06/21/4:24 下午
 * @Description:
 * @Version:
 */
@Data
public class BusinessAlertsUserMap implements Serializable {

    private static final long serialVersionUID = -119775768767246980L;
    private String appName;

    private String userId;

    /**
     * 用户Id:规则10001
     */
    private String teamLeaderId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号码的国际区号
     */
    private String callingPrefix;

    /**
     * 收件人手机号
     */
    private String phone;

    /**
     * 注册时候Email
     */
    private String email;
    /**
     * 域账号
     */
    private String domainAccount;
}
