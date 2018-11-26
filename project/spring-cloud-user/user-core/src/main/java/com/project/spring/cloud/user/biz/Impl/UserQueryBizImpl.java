package com.project.spring.cloud.user.biz.impl;

import com.project.spring.cloud.common.cache.RedisClient;
import com.project.spring.cloud.user.biz.UserQueryBiz;
import com.project.spring.cloud.user.domain.User;
import com.project.spring.cloud.user.service.UserService;
import com.util.msf.rpc.common.BusinessException;
import com.util.msf.rpc.common.ResultCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wencheng on 2018/11/26.
 */
@Service("userQueryBiz")
public class UserQueryBizImpl implements UserQueryBiz {

    private static final int LOGIN_EXPIRE_S = 60 * 30;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisClient redisClient;

    @Override
    public User queryByUserId(Long userId) {
        return userService.queryById(userId);
    }

    @Override
    public Long queryLoginUserIdByAccessToken(String accessToken) {
        String userIdStr = redisClient.get(accessToken);
        if (StringUtils.isBlank(userIdStr)) {
            throw BusinessException.of(ResultCode.UserNoLogin);
        }
        redisClient.expire(accessToken, LOGIN_EXPIRE_S);
        return Long.valueOf(userIdStr);
    }

    @Override
    public Boolean checkLoginStatusByAccessToken(String accessToken) {
        Boolean exists = redisClient.exists(accessToken);
        return exists;
    }
}
