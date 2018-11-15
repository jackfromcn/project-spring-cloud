package com.project.spring.cloud.gateway.config.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wencheng on 2018/11/15.
 */
@Configuration
@ConfigurationProperties(prefix = "login.validate")
@RefreshScope
@Data
@Slf4j
public class LoginValidateProperties {

    private Patterns patterns;

    private boolean required;

    @Data
    public static class Patterns {
        private String[] exclude;
        private String[] include;
    }
}
