package com.project.spring.cloud.common.interceptor;

import com.project.spring.cloud.common.annotation.AopLog;
import com.util.msf.core.utils.JsonUtils;
import com.util.msf.rpc.common.BusinessException;
import com.util.msf.rpc.common.Result;
import com.util.msf.rpc.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author wencheng
 * @ClassName LogInterceptor
 * @Description 方法拦截器日志打印
 * @date 2018-05-11 下午5:07
 */
@Slf4j
public class LogInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();
        String clazz = invocation.getMethod().getDeclaringClass().getSimpleName();
        Object[] args = invocation.getArguments();

        AopLog aopLog = invocation.getMethod().getAnnotation(AopLog.class);
        aopLog = aopLog == null ? invocation.getMethod().getDeclaringClass().getAnnotation(AopLog.class) : aopLog;
        boolean isPrint = aopLog == null || aopLog.isPrint();
        String desc = aopLog != null ? aopLog.value() : "";
        long begin = System.currentTimeMillis();
        if (isPrint && log.isInfoEnabled()) {
            log.info("[monitor]-[{}.{}-{}]--begin, args={}", clazz, methodName, desc, JsonUtils.toJson(args));
        }
        try {
            Object result = invocation.proceed();
            if (isPrint && log.isInfoEnabled()) {
                long cost = System.currentTimeMillis() - begin;
                log.info("[monitor]-[{}.{}-{}]--end, cost={}ms, args={}, result={}", clazz, methodName, desc, cost,
                        JsonUtils.toJson(args), JsonUtils.toJson(result));
            }
            return result;
        } catch (BusinessException e) {
            long cost = System.currentTimeMillis() - begin;
            if (isPrint) {
                log.error("[monitor]-[{}.{}-{}]--business exception, cost={}ms, args={}", clazz, methodName, desc, cost,
                        JsonUtils.toJson(args), e);
            }

            return Result.of(e);
        } catch (Throwable e) {
            long cost = System.currentTimeMillis() - begin;
            log.error("[monitor]-[{}.{}-{}]--application exception, cost={}ms, args={}", clazz, methodName, desc, cost,
                    JsonUtils.toJson(args), e);

            return ResultCode.Exception.result("服务端异常");
        }
    }
}
