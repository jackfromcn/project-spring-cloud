package com.project.spring.cloud.common.route;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.util.msf.core.constant.Const;
import com.util.msf.core.helper.WebHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;

import javax.servlet.http.HttpServletResponse;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;

/**
 * @author wencheng
 * @ClassName AbstractPostFilter
 * @Description zuul过滤器
 * @date 2017-11-22 下午3:43
 */
@Slf4j
public abstract class AbstractPostFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public abstract Object run();

    protected Object defaultRun() {
        RequestContext context = getCurrentContext();
        HttpServletResponse response = context.getResponse();
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        response.setCharacterEncoding(Const.UTF8);
        WebHelper.destroy();
        return null;
    }
}
