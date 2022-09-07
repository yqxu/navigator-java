package com.pingpongx.smb.warning.dal.dataobject;

import com.pingpongx.business.dal.core.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 业务监控告警负责人信息表
 * </p>
 *
 * @author PingPong Code Generator
 * @since 2022-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BusinessAlertsToReceiver extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 应用名称
     */
    @TableField("appName")
    private String appName;

    /**
     * 收件人名称
     */
    @TableField("receiverName")
    private String receiverName;

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


}
