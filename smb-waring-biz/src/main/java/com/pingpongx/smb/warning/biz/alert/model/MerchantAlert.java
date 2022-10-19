package com.pingpongx.smb.warning.biz.alert.model;

import com.pingpongx.smb.warning.biz.constant.Constant;
import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import lombok.Data;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * {
 * 		"application": "user-web",
 * 		"content": "[2022-10-14 10:45:05,743] [WARN] [ExceptionHelper:101] [http-nio-8010-exec-82] -- [TID: 6188.437.16657155057425887] 202.101.191.115 ; 181008110918153 POST-/api/user/web/login httpMessageNotReadableException\norg.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: not match : - \u001a, info : pos 462, line 1, column 463{\"loginType\":\"email_password\",\"password\":\"gkHdFmdUxr9/8PEHTmpmbmXHojEji+odLaLcH8WB6BPqQ3j2X30QaWFaTwMZOyrHlY2Qm/XWgvI53+QPhoT23Tfl3flGxnqzVjj9IyEhRt+lJxD38Ev0n/doyDlBlCNFTeIytX1rGP1VII8vr6jirBK4LGKgcYFwgijtk8Zu7N7D3YwQC/eWdtTD5wfwOEFoWxX3YYuS1h5Tq1XlFqFEW31VAgOYzH3CRkZG0auvWs9DCHkTT1SM4kdkqOmqgkrEeQhySPFnZWr1qx7Mb6N6FCoCc0tHKjxITeEJOqjK/m1EVIZhbKtykxBDlFnB0TBoU5Ex457H7nJ0Y5ssc1OA7g==\",\"phone\":\"\",\"email\":\"51609576-lw070902@pingpongx.com\",\"callingPrefix\":\"+86\"; nested exception is com.alibaba.fastjson.JSONException: not match : - \u001a, info : pos 462, line 1, column 463{\"loginType\":\"email_password\",\"password\":\"gkHdFmdUxr9/8PEHTmpmbmXHojEji+odLaLcH8WB6BPqQ3j2X30QaWFaTwMZOyrHlY2",
 * 		"total": "1"
 * }
 */
@Data
public class MerchantAlert implements ThirdPartAlert{

    @Override
    public IdentityPath<String> countPath() {
        return IdentityPath.of(Stream.of(throwAppName()).collect(Collectors.toList()));
    }
    private static final long serialVersionUID = -5136469378192983192L;


    String application;

    String content;
    @Override
    public String throwAppName() {
        return application;
    }

    @Override
    public String throwContent() {
        String ret = "appName:"+application+"\n"+content;
        return ret;
    }

    @Override
    public String depart() {
        return Constant.Merchant.name;
    }

    @Override
    public void departSet(String depart) {
    }
}
