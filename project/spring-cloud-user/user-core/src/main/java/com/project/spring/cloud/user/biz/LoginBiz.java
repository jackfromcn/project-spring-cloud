package com.project.spring.cloud.user.biz;

import com.project.spring.cloud.user.dto.UserDto;

/**
 * Created by wencheng on 2018/11/26.
 */
public interface LoginBiz {

    /**
     * 用户登录
     * @param loginName
     * @param password
     * @return
     */
    UserDto login(String loginName, String password);

    /**
     * 用户登出
     * @param accessToken 登录验签
     * @return
     */
    Boolean logout(String accessToken);

}
