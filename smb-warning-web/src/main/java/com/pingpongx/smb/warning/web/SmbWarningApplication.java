package com.pingpongx.smb.warning.web;

import com.google.common.collect.Lists;
import com.pingpongx.flowmore.cloud.base.server.app.BaseApplicationDocable;
import com.pingpongx.flowmore.cloud.base.server.app.BaseApplicationInfo;
import com.pingpongx.flowmore.cloud.base.server.app.BaseApplicationRun;
import com.pingpongx.smb.warning.biz.constants.ServiceConstants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.service.ApiInfo;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/06/7:11 下午
 * @Description: 启动类
 * @Version:
 */
@SpringBootApplication(scanBasePackages = {"com.pingpongx.smb.warning", "com.pingpongx.passwordcallback"})
public class SmbWarningApplication implements BaseApplicationDocable {

    @Override
    @Bean
    public BaseApplicationInfo getAppInfo() {
        ApiInfo apiInfo = new ApiInfo("Api", "Api", "1.0", null, null, null, null, Lists.newArrayList());
        return new BaseApplicationInfo(apiInfo, ServiceConstants.APP_NAME);
    }

    public static void main(String[] args) {
        BaseApplicationRun.run(args, SmbWarningApplication.class);
    }

}
