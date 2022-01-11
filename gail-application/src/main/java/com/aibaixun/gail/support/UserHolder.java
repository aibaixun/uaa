package com.aibaixun.gail.support;

import com.aibaixun.basic.entity.BaseAuthUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserHolder {

    public String getCurrentUserId() {
        BaseAuthUser currentUser = getCurrentUser();

        if (currentUser != null) {
            return currentUser.getUserId();
        }
        return null;
    }

    public String getCurrentUserTenantId() {
        BaseAuthUser currentUser = getCurrentUser();

        if (currentUser != null) {
            return currentUser.getTenantId();
        }
        return null;
    }

    public String getCurrentUserType() {
        BaseAuthUser currentUser = getCurrentUser();

        if (currentUser != null) {
            return currentUser.getType();
        }
        return "";
    }

    public BaseAuthUser getCurrentUser() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof BaseAuthUser) {
            return (BaseAuthUser) user;
        } else {
            return null;
        }
    }
}
