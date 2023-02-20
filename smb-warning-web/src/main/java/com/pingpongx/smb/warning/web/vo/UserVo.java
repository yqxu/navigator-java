package com.pingpongx.smb.warning.web.vo;

import com.baomidou.mybatisplus.annotation.TableField;

public class UserVo {


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
     * 手机号
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
