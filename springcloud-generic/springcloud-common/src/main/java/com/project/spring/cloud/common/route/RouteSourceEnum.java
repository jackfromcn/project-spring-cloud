package com.project.spring.cloud.common.route;

/**
 * @author wencheng
 * @ClassName RouteSourceEnum
 * @Description 请求来源
 * @date 2017-12-01 下午6:29
 */
public enum RouteSourceEnum {
    UNKNOWN(0, "未知"),
    USER(1, "用户端"),
    OPERATION(2, "运营端")
    ,;

    RouteSourceEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int code;
    private String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
