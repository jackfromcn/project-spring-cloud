package com.project.spring.cloud.user.service.impl;

import com.project.spring.cloud.user.domain.User;
import com.project.spring.cloud.user.mapper.UserMapper;
import com.project.spring.cloud.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wencheng on 2018/11/23.
 */
@Service()
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User queryByNameAndPassword(String loginName, String password) {
        return userMapper.selectByLoginNameAndPassword(loginName, password);
    }
}
