package com.project.spring.cloud.user.facade.impl;

import com.project.spring.cloud.user.biz.UserQueryBiz;
import com.project.spring.cloud.user.domain.User;
import com.project.spring.cloud.user.dto.UserDto;
import com.project.spring.cloud.user.facade.UserQueryFacade;
import com.util.msf.core.convert.Converter;
import com.util.msf.rpc.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wencheng on 2018/11/15.
 */
@RestController
@Slf4j
public class UserQueryFacadeImpl implements UserQueryFacade {

    @Autowired
    private UserQueryBiz userQueryBiz;

    @Override
    public Result<UserDto> queryById(@PathVariable("id") Long id) {
        User user = userQueryBiz.queryByUserId(id);
        return Result.succeed(Converter.map(user, UserDto.class));
    }
}
