package com.project.spring.cloud.user.facade.impl;

import com.project.spring.cloud.user.biz.LoginBiz;
import com.project.spring.cloud.user.biz.UserQueryBiz;
import com.project.spring.cloud.user.dto.UserDto;
import com.project.spring.cloud.user.facade.UserValidateFacade;
import com.util.msf.rpc.common.Result;
import com.util.msf.rpc.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wencheng on 2018/11/15.
 */
@RestController
@Slf4j
public class UserValidateFacadeImpl implements UserValidateFacade {

    @Autowired
    private UserQueryBiz userQueryBiz;

    @Autowired
    private LoginBiz loginBiz;

    @Override
    public Result<Boolean> checkToken(@RequestHeader("accessToken") String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return ResultCode.ParamNonExist.result();
        }
        return Result.succeed(userQueryBiz.checkLoginStatusByAccessToken(accessToken));
    }

    @Override
    public Result<Long> queryUserIdByToken(@RequestHeader("accessToken") String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return ResultCode.ParamNonExist.result();
        }
        return Result.succeed(userQueryBiz.queryLoginUserIdByAccessToken(accessToken));
    }

    @Override
    public Result<UserDto> login(@RequestParam("account") String account, @RequestParam("password") String password) {
//        UserDto userDto = new UserDto().setId(1L).setName("管理员").setLoginName("管理员1").setMobile("18710081678")
//                .setEmail("spring.cloud@example.com").setAccessToken("123456");
        return Result.succeed(loginBiz.login(account, password));
    }

    @Override
    public Result<Boolean> logout(@RequestHeader("accessToken") String accessToken) {
        return Result.succeed(loginBiz.logout(accessToken));
    }
}
