package com.project.spring.cloud.user.biz;

import com.project.spring.cloud.user.domain.User;

/**
 * Created by wencheng on 2018/11/26.
 */
public interface UserQueryBiz {

    /**
     * 根据userId查询 User 信息
     * @param userId
     * @return
     */
    User queryByUserId(Long userId);

    /**
     * 根据accessToken获取登录userId
     * @param accessToken 登录验签
     * @return
     */
    Long queryLoginUserIdByAccessToken(String accessToken);

    /**
     * 根据accessToken判断是否登录
     * @param accessToken
     * @return
     */
    Boolean checkLoginStatusByAccessToken(String accessToken);

}
