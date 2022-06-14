package com.aibaixun.uaa.support;

import com.aibaixun.basic.entity.AuthUserInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserHolder {

    public String getCurrentUserId() {
        AuthUserInfo currentUser = getCurrentUser();

        if (currentUser != null) {
            return currentUser.getUserId();
        }
        return null;
    }

    public String getCurrentUserTenantId() {
        AuthUserInfo currentUser = getCurrentUser();

        if (currentUser != null) {
            return currentUser.getTenantId();
        }
        return null;
    }

    public String getCurrentUserType() {
        AuthUserInfo currentUser = getCurrentUser();

        if (currentUser != null) {
            return currentUser.getType();
        }
        return "";
    }

    public AuthUserInfo getCurrentUser() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof AuthUserInfo) {
            return (AuthUserInfo) user;
        } else {
            return null;
        }
    }
}
