package com.project.spring.cloud.common.helper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WebHelper {

    public static final String LOGIN_NAME = "loginName";
    public static final String USER_ID = "userId";
    private static boolean loginRequrired = true;
    public static final String USER_SESSION = "userSession";

    private UserSession userSession;
    private static Long defaultUserId = 0L;

    private static Map<String, Object> emptyToken = Collections.emptyMap();

    private static final ThreadLocal<Map<String, Object>> loginContext = new ThreadLocal<Map<String, Object>>();

    public static void init(String loginName, Long userId, UserSession userSession) {
        if (loginContext.get() == null) {
            loginContext.set(getMap(LOGIN_NAME, loginName, USER_ID, userId, USER_SESSION, userSession));
        } else {
            loginContext.get().put(LOGIN_NAME, loginName);
            loginContext.get().put(USER_ID, userId);
            loginContext.get().put(USER_SESSION, userSession);
        }
    }

    public static void destroy() {
        loginContext.remove();
    }

    /**
     * 获取登录用户ID
     *
     * @return
     */
    public static Long getUserId() {
        if (!loginRequrired) {
            return defaultUserId;
        }
        if (loginContext.get() == null) {
            return null;
        }
        return (Long) loginContext.get().get(USER_ID);
    }

    private static Map<String, Object> getMap(String k, Object o, String k1, Object o1, String k2, Object o2) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(k, o);
        map.put(k1, o1);
        map.put(k2, o2);
        return map;
    }

    public static void setLoginRequrired(boolean loginRequrired) {
        WebHelper.loginRequrired = loginRequrired;
    }

    public static boolean isLoginRequrired() {
        return loginRequrired;
    }

    public static UserSession getUserSession(){
        return loginContext.get().get(USER_SESSION) == null? null: (UserSession)loginContext.get().get(USER_SESSION);
    }

}
