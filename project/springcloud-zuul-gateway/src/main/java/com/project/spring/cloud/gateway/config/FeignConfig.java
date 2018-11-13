package com.project.spring.cloud.gateway.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wencheng
 * @since 2018/5/22
 */
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor bizSourceRequestInterceptor() {
        return template -> template.header("bizSource", "chejinrongcduan");
    }

}
