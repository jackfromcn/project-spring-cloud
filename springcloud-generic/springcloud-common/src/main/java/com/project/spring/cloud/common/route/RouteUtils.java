package com.project.spring.cloud.common.route;

import com.util.msf.core.crypto.Cryptos;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author wencheng
 * @ClassName RouteUtils
 * @Description 路由工具类
 * @date 2017-11-22 下午7:42
 */
public class RouteUtils {

    /**
     * 获取request中userId
     *
     * @return userId
     */
    public static Optional<Long> getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userId = request.getHeader(RouteConstant.USER_ID);
        if (StringUtils.isBlank(userId)) {
            return Optional.empty();
        } else {
            userId = Cryptos.aesDecrypt(userId);
        }
        return Optional.ofNullable(Long.parseLong(userId));
    }

    /**
     * 获取dataScope
     *
     * @return dataScope
     */
    public static Optional<Integer> getDataScope() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String dataScope = request.getHeader(RouteConstant.DATA_SCOPE);
        if (StringUtils.isBlank(dataScope)) {
            return Optional.empty();
        } else {
            dataScope = Cryptos.aesDecrypt(dataScope);
        }
        return Optional.ofNullable(Integer.parseInt(dataScope));
    }

    /**
     * 获取请求来源
     *
     * @return source
     */
    public static Optional<Integer> getSource() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String source = request.getHeader(RouteConstant.SOURCE);
        if (StringUtils.isBlank(source)) {
            return Optional.empty();
        } else {
            source = Cryptos.aesDecrypt(source);
        }
        return Optional.ofNullable(Integer.parseInt(source));
    }
}
