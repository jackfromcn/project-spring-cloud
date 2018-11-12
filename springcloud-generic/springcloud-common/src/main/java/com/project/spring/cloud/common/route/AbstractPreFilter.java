package com.project.spring.cloud.common.route;

import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author wencheng
 * @ClassName AbstractPreFilter
 * @Description AbstractPreFilter
 * @date 2017-11-22 下午3:43
 */
@Slf4j
public abstract class AbstractPreFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public abstract Object run();

}
