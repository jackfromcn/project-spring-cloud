package com.project.spring.cloud.gateway.filter;

import com.project.spring.cloud.common.route.AbstractPostFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wencheng
 * @ClassName AccessPostFilter
 * @Description zuul过滤器
 * @date 2018-03-22 下午5:17
 */
@Component
@Slf4j
public class AccessPostFilter extends AbstractPostFilter {

    @Override
    public Object run() {
        return super.defaultRun();
    }
}
