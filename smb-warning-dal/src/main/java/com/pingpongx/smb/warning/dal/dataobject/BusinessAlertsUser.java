package com.pingpongx.smb.warning.dal.dataobject;

import com.pingpongx.business.dal.core.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 开发负责人信息表
 * </p>
 *
 * @author PingPong Code Generator
 * @since 2022-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BusinessAlertsUser extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户Id:规则10001
     */
    @TableField("userId")
    private String userId;

    /**
     * 用户Id:规则10001
     */
    @TableField("teamLeaderId")
    private String teamLeaderId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号码的国际区号
     */
    @TableField("callingPrefix")
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
    @TableField("domainAccount")
    private String domainAccount;


}
