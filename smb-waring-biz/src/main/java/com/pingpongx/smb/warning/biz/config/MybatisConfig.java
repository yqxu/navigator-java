package com.pingpongx.smb.warning.biz.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import javax.sql.DataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/06/7:07 下午
 * @Description:
 * @Version:
 */
@Configuration
@MapperScan("com.pingpongx.smb.warning.dal.mapper")
public class MybatisConfig {

    private final DataSource dataSource;

    public MybatisConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }
}
