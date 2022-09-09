package com.pingpongx.smb.warning.web.config;

import com.pingpongx.smb.warning.web.filter.WarningWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/09/10:35 上午
 * @Description:
 * @Version:
 */

@Configuration
public class WarningWebConfig {

    @Bean
     public WarningWebFilter warningWebFilter(){
         return new WarningWebFilter();
     }
}
