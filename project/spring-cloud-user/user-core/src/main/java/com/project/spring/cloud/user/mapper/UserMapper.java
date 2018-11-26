package com.project.spring.cloud.user.mapper;

import com.project.spring.cloud.user.domain.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByLoginNameAndPassword(@Param("loginName") String loginName, @Param("password") String password);
}