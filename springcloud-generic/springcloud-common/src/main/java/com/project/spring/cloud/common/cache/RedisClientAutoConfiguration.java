package com.project.spring.cloud.common.cache;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author wencheng
 * @since $date
 */
@Configuration
@ConditionalOnClass(RedisConnectionFactory.class)
@EnableAutoConfiguration
public class RedisClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RedisClient.class)
    public RedisClient redisClient(RedisConnectionFactory redisConnectionFactory) {
        return new RedisClient(redisConnectionFactory);
    }
}
