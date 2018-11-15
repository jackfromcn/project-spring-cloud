package com.project.spring.cloud.user.config;

import com.project.spring.cloud.common.filter.LogFilter;
import com.project.spring.cloud.common.interceptor.LogInterceptor;
import com.util.msf.rpc.common.serializer.ModuleFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;
import java.util.Optional;

@Configuration
public class LocalWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new LogFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.getObjectMapper().registerModule(ModuleFactory.createFrontend());
        Optional<HttpMessageConverter<?>> converterOptional = converters.stream()
                .filter(c -> c instanceof MappingJackson2HttpMessageConverter).findFirst();
        if (converterOptional.isPresent()) {//endpoint 均是*+json类型，例如prometheus，不能json序列化，置于StringHttpMessageConverter之后
            int index = converters.indexOf(converterOptional.get());
            converters.add(index, messageConverter);
        } else {
            converters.add(messageConverter);
        }

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
    }

}
