package com.project.spring.cloud.gateway.filter;

import com.netflix.zuul.context.RequestContext;
import com.project.spring.cloud.common.route.AbstractPreFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhaozhenkuan
 * @ClassName AccessPreFilter
 * @Description zuul过滤器
 * @date 2018-03-22 下午5:16
 */
@Component
@Slf4j
public class AccessPreFilter extends AbstractPreFilter {

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        String userId = null, userIdStr = null;

        log.info("访问的url:{}", request.getRequestURI());

        return null;
    }
}
