package com.pingpongx.smb.warning.web.module;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/07/10:55 上午
 * @Description:
 * @Version:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AlertUser implements Serializable {

    private static final long serialVersionUID = -2380970307914378215L;


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
