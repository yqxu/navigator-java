package com.pingpongx.smb.warning.api.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/06/7:54 下午
 * @Description:
 * @Version:
 */
@Data
public class AlertUserDTO implements Serializable {


    private static final long serialVersionUID = -4120191458336033818L;
    /**
     * 邮箱数据
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;
    /**
     * 手机号前缀
     */
    private String callingPrefix;

    /**
     * 用户编号
     */
    private String userId;
    /**
     * 领导人
     */
    private String teamLeaderId;

    /**
     * 姓名
     */
    private String name;
    /**
     * 域账号信息
     */
    private String domainAccount;
}
