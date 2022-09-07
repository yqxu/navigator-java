package com.pingpongx.smb.warning.biz.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.pingpongx.passwordcallback.DbPasswordCallback;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/06/7:06 下午
 * @Description:
 * @Version:
 */
@Configuration
public class DataSourceConfig {

    @Lazy
    private final DbPasswordCallback dbPasswordCallback;

    public DataSourceConfig(DbPasswordCallback dbPasswordCallback) {
        this.dbPasswordCallback = dbPasswordCallback;
    }

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource dataSourceOne(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setPasswordCallback( dbPasswordCallback );
        return druidDataSource;
    }

}
