package com.pingpongx.smb.warning.api.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: jiangkun
 * @Date: 2022/06/21/5:21 下午
 * @Description:
 * @Version:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DingDReceiverDTO implements Serializable {

    private static final long serialVersionUID = -4771954588563674660L;
    /**
     * 手机号前缀
     */
    private String code;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 域账号
     */
    private String domainAccount;

    public static DingDReceiverDTO getDefaultUser() {
        // 暂时默认老黑
        return DingDReceiverDTO.builder().code("86").phone("15757115779").email("tongzx@pingpongx.com").domainAccount("tongzx").build();
    }
}
