package com.project.spring.cloud.user.service;

import com.project.spring.cloud.user.domain.User;

/**
 * Created by wencheng on 2018/11/23.
 */
public interface UserService {

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User queryById(Long id);

    /**
     * 根据登录名和密码获取用户信息
     * @param loginName
     * @param password
     * @return
     */
    User queryByNameAndPassword(String loginName, String password);

}
