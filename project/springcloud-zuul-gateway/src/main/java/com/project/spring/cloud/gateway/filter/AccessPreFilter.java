package com.project.spring.cloud.gateway.filter;

import com.netflix.zuul.context.RequestContext;
import com.project.spring.cloud.common.route.AbstractPreFilter;
import com.project.spring.cloud.gateway.config.properties.LoginValidateProperties;
import com.project.spring.cloud.gateway.rpc.fegin.UserValidateClient;
import com.project.spring.cloud.user.utils.UserValidateUtil;
import com.util.msf.core.helper.WebHelper;
import com.util.msf.rpc.common.BusinessException;
import com.util.msf.rpc.common.Result;
import com.util.msf.rpc.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author wencheng
 * @ClassName AccessPreFilter
 * @Description zuul过滤器
 * @date 2018-03-22 下午5:16
 */
@Component
@Slf4j
public class AccessPreFilter extends AbstractPreFilter {

    @Autowired
    private LoginValidateProperties loginValidateProperties;

    @Autowired
    private PathMatcher pathMatcher;

    @Autowired
    private UserValidateClient userValidateClient;

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String url = request.getRequestURI();
        LoginValidateProperties.Patterns patterns = loginValidateProperties.getPatterns();
        if (patterns == null) {
            return true;
        }
        if (patterns.getExclude() != null) {
            for (String pattern : patterns.getExclude()) {
                if (pathMatcher.match(pattern, url)) {
                    return false;
                }
            }
        }
        if (ObjectUtils.isEmpty(patterns.getInclude())) {
            return true;
        } else {
            for (String pattern : patterns.getInclude()) {
                if (pathMatcher.match(pattern, url)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        log.info("访问的url:{}", request.getRequestURI());
        if (loginValidateProperties.isRequired()) {
            String accessToken = UserValidateUtil.getAccessTokenInRequest(request);
            if (StringUtils.isBlank(accessToken)) {
                throw BusinessException.of(ResultCode.UserNoLogin);
            }
            Result<Long> result = userValidateClient.queryUserIdByToken(accessToken);
            if (!result.isSucceed()) {
                throw BusinessException.of(result.getCode(), result.getMsg());
            }
            WebHelper.init(result.getData());
        } else {
            WebHelper.init("anonymous", 0L);
        }

        Optional.ofNullable(WebHelper.getUserId()).ifPresent(userIdOp -> context.addZuulRequestHeader(WebHelper.USER_ID, userIdOp.toString()));
        return null;
    }
}
