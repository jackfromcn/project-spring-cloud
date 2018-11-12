package com.project.spring.cloud.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: AopLog
 * @Description: 日志注解
 * @author: wencheng
 * @date: 2017-08-13 下午10:56
 * @version: V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AopLog {
    /**
     * 日志描述
     *
     * @return
     */
    String value() default "";

    /**
     * 是否打印日志内容
     *
     * @return
     */
    boolean isPrint() default true;

}
