package com.aibaixun.gail.entity;

import java.io.Serializable;

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
        USERNAME,
        EMAIL,
        MOBILE,
        TOKEN,
        REFLASH
    }
}