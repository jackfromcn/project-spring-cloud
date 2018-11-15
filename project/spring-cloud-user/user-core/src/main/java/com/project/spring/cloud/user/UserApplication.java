package com.project.spring.cloud.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by wencheng on 2018/11/13.
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.util.msf", "com.project.spring.cloud"})
@Slf4j
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        log.info("==================================");
        log.info("User Server startup");
        log.info("==================================");
    }

}
