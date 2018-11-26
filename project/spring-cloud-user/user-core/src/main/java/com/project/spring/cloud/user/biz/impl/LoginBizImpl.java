package com.project.spring.cloud.user.biz.impl;

import com.project.spring.cloud.common.cache.RedisClient;
import com.project.spring.cloud.user.biz.LoginBiz;
import com.project.spring.cloud.user.domain.User;
import com.project.spring.cloud.user.dto.UserDto;
import com.project.spring.cloud.user.service.UserService;
import com.util.msf.core.convert.Converter;
import com.util.msf.core.utils.N;
import com.util.msf.rpc.common.BusinessException;
import com.util.msf.rpc.common.ResultCode;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wencheng on 2018/11/26.
 */
@Service("loginBiz")
public class LoginBizImpl implements LoginBiz {

    private static final int LOGIN_EXPIRE_S = 60 * 30;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisClient redisClient;

    @Override
    public UserDto login(String loginName, String password) {
        if (N.anyNull(loginName, password)) {
            throw BusinessException.of(ResultCode.ParamNonExist);
        }
        User user = userService.queryByNameAndPassword(loginName, password);
        if (user == null) {
            throw BusinessException.of(ResultCode.UserNonExist);
        }
        user.setPassword(null);

        String accessToken = DigestUtils.md5Hex(user.getId()+loginName+System.currentTimeMillis());
        redisClient.setex(accessToken, user.getId().toString(), LOGIN_EXPIRE_S);
        UserDto userDto = Converter.map(user, UserDto.class);
        userDto.setAccessToken(accessToken);
        return userDto;
    }

    @Override
    public Boolean logout(String accessToken) {
        Boolean exists = redisClient.exists(accessToken);
        if (!exists) {
            return true;
        }

        redisClient.del(accessToken);
        return true;
    }
}
