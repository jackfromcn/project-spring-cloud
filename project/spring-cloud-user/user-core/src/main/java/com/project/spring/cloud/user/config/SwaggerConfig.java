package com.project.spring.cloud.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName: SwaggerConfig
 * @Description: Swagger2配置
 * @author: wencheng
 * @date: 2017-09-08 下午6:02
 * @version: V1.0
 */
@Profile("!prod")
@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("user").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.project.spring.cloud.user.facade")).paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("用户服务").description("用户服务API").version("1.0").build();
    }

}
