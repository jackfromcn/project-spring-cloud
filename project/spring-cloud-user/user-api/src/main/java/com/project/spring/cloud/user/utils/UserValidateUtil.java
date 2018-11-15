package com.project.spring.cloud.user.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wencheng on 2018/11/15.
 */
public class UserValidateUtil {

    public static String getAccessTokenInRequest(HttpServletRequest request) {
        String token = request.getHeader("accessToken");
        return token;
    }

}
