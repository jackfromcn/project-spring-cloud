package com.project.spring.cloud.common.controller;

import com.util.msf.core.utils.JsonUtils;
import com.util.msf.core.utils.T;
import com.util.msf.core.web.ServletUtils;
import com.util.msf.log.alarm.Alarmer;
import com.util.msf.rpc.common.BusinessException;
import com.util.msf.rpc.common.Result;
import com.util.msf.rpc.common.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * mvc统一请求异常处理
 *
 * @author iwang
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    private static Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    private static boolean alarmed = false;

    /**
     * 是否告警
     *
     * @param alarmed
     */
    public static void setAlarmed(boolean alarmed) {
        ExceptionControllerAdvice.alarmed = alarmed;
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Result<T> handleBusinessException(BusinessException exception, HttpServletRequest request) {
        logger.error("http request BusinessException, uri={}, args={}, remoteIP={}", request.getRequestURI(), JsonUtils.toJson(request.getParameterMap()), ServletUtils.IP(request), exception);
        return Result.of(exception.getCode(), exception.getMsg());
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public Result<T> handleException(MissingServletRequestParameterException exception, HttpServletRequest request) {
        logger.error("http request MissingServletRequestParameterException, uri={}, args={}, remoteIP={}", request.getRequestURI(), JsonUtils.toJson(request.getParameterMap()), ServletUtils.IP(request), exception);
        return ResultCode.ParamNonExist.result();
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result<T> handleException(HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        logger.error("http request HttpRequestMethodNotSupportedException, uri={}, args={}, remoteIP={}", request.getRequestURI(), JsonUtils.toJson(request.getParameterMap()), ServletUtils.IP(request), exception);
        return ResultCode.Exception.result();
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result<T> handleException(Exception exception, HttpServletRequest request) {
        logger.error("http request exception, uri={}, args={}, remoteIP={}", request.getRequestURI(), JsonUtils.toJson(request.getParameterMap()), ServletUtils.IP(request), exception);
        if (alarmed) {
            Alarmer.defaultAlarmer().send("http request exception, uri={}, args={}, remoteIP={}", request.getRequestURI(), JsonUtils.toJson(request.getParameterMap()), ServletUtils.IP(request), exception);
        }
        return ResultCode.Exception.result();
    }
}
