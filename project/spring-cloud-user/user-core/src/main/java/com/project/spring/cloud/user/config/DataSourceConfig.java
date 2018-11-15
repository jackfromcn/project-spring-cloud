package com.project.spring.cloud.user.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * 数据源及事务配置
 *
 * @author wencheng
 */
@Configuration
@MapperScan(basePackages = "com.project.spring.cloud.user.mapper")
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Configuration
    @EnableTransactionManagement
    public static class TransactionConfig {
        @Autowired
        private DataSourceTransactionManager transactionManager;

        @Bean
        @Primary
        public TransactionTemplate transactionTemplate() {
            return new TransactionTemplate(transactionManager);
        }

    }
}
