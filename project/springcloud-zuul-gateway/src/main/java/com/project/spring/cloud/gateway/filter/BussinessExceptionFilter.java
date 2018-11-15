//package com.project.spring.cloud.gateway.filter;
//
//import com.netflix.zuul.context.RequestContext;
//import com.project.spring.cloud.common.route.AbstractErrorFilter;
//import com.util.msf.rpc.common.BusinessException;
//import com.util.msf.rpc.common.ResultCode;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
///**
// * @author zhaozhenkuan
// * @ClassName AccessErrorFilter
// * @Description zuul过滤器
// * @date 2018-03-22 下午5:17
// */
//@Component
//@Slf4j
//public class BussinessExceptionFilter extends AbstractErrorFilter {
//
//    @Override
//    public boolean shouldFilter() {
//        RequestContext context = RequestContext.getCurrentContext();
//        Throwable throwable = context.getThrowable();
//        if (throwable.getCause() instanceof BusinessException) {
//            return true;
//        }
//        return true;
//    }
//
//    @Override
//    public Object run() {
//        throw BusinessException.of(ResultCode.UserNoLogin);
//    }
//}
