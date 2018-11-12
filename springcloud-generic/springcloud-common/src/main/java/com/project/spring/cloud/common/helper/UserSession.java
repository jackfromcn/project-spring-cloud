package com.project.spring.cloud.common.helper;

public class UserSession {

    private Long userId;
    private String username;
    private String realname;
    private String mobile;
    private String pic;

    public UserSession() {
    }

    public UserSession(Long userId, String username, String realname, String mobile) {
        this.userId = userId;
        this.username = username;
        this.realname = realname;
        this.mobile = mobile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
