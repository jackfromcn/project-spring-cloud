package com.project.spring.cloud.common.route;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.util.msf.core.constant.Const;
import com.util.msf.core.utils.JsonUtils;
import com.util.msf.core.web.ServletUtils;
import com.util.msf.rpc.common.BusinessException;
import com.util.msf.rpc.common.Result;
import com.util.msf.rpc.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * @author wencheng
 * @ClassName AccessErrorFilter
 * @Description AbstractErrorFilter
 * @date 2017-11-22 下午3:43
 */
@Slf4j
public abstract class AbstractErrorFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public abstract Object run();


    protected Object defaultRun() {
        RequestContext context = getCurrentContext();
        HttpServletRequest request = context.getRequest();
        log.error("【zuul路由错误处理】- zuul路由发生异常, uri={}, remoteAddress={}", request.getRequestURI(), ServletUtils.IP(request), context.getThrowable());
        log.error("【zuul路由错误处理】- zuul路由发生异常, contentType={}, contentLength={}", request.getContentType(), request.getContentLength());
        log.error("【zuul路由错误处理】- zuul路由发生异常, queryString={}", ServletUtils.getQueryString(request));
        PrintWriter writer = null;
        try {
            HttpServletResponse response = context.getResponse();
            response.setContentType(ContentType.APPLICATION_JSON.toString());
            response.setCharacterEncoding(Const.UTF8);
            writer = response.getWriter();
            Throwable t = context.getThrowable() == null ? null : context.getThrowable().getCause();
            if (t != null && t instanceof BusinessException) {
                writer.write(JsonUtils.toJson(Result.of((BusinessException) t)));
            } else {
                writer.write(JsonUtils.toJson(ResultCode.Exception.result()));
            }
            writer.flush();
        } catch (Exception e) {
            log.error("【zuul路由错误处理】- 回写HTTP响应异常, uri={}", request.getRequestURI(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        context.setSendZuulResponse(false);
        return null;
    }

}
