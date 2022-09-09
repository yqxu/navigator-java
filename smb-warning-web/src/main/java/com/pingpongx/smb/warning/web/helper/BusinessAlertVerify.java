package com.pingpongx.smb.warning.web.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/09/11:02 上午
 * @Description:
 * @Version:
 */
@Component
public class BusinessAlertVerify {

    private static final String SLS_SIGN = "b2e36fdf8981850f8ed8c0de87119c56";

    /**
     * 验证请求头信息
     * @param sign 签名数据
     * @return 返回验证结果
     */
    public boolean checkRequest(String sign){
        return !StringUtils.isEmpty(sign) && SLS_SIGN.equals(sign);
    }
}
