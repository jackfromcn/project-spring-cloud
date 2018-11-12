package com.project.spring.cloud.common.filter;

import com.util.msf.core.utils.JsonUtils;
import com.util.msf.core.web.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author wencheng
 * @ClassName LogFilter
 * @Description 过滤器记录log
 * @date 2018-05-11 下午5:07
 */
@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest resq = (HttpServletRequest) servletRequest;
        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
            long cost = System.currentTimeMillis() - start;
            log.info("[log]-request end, uri={}, cost={}ms, args={}, remoteIP={}", resq.getRequestURI(), cost,
                    JsonUtils.toJson(resq.getParameterMap()), ServletUtils.IP(resq));
        } catch (Throwable e) {
            long cost = System.currentTimeMillis() - start;
            log.error("[log]-occur exception, uri={}, cost={}ms, args={}, remoteIP={}", resq.getRequestURI(), cost,
                    JsonUtils.toJson(resq.getParameterMap()), ServletUtils.IP(resq), e);

        }
    }

    @Override
    public void destroy() {

    }
}
