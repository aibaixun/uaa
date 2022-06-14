package com.aibaixun.uaa.auth;

import java.io.Serializable;

/**
 * @author wangxiao
 */
public class UserPrincipal implements Serializable {

    private final Type type;
    private final String value;

    public UserPrincipal(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public enum Type {
        /**
         * 用户名
         */
        USERNAME,

        /**
         * 邮箱
         */
        EMAIL,

        /**
         * 手机号
         */
        MOBILE,

        /**
         * token
         */
        TOKEN,

        /**
         * 刷新token
         */
        REFRESH
    }
}
