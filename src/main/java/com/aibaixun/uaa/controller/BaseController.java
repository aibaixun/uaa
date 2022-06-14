package com.aibaixun.uaa.controller;

import com.aibaixun.basic.entity.AuthUserInfo;
import com.aibaixun.uaa.support.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;

import static com.aibaixun.uaa.auth.SecurityConstants.ADMIN_TYPE;

/**
 * @author wangxiao
 */
public abstract class BaseController {

    private UserHolder userHolder;

    protected String getCurrentUserId (){
        return userHolder.getCurrentUserId();
    }

    protected String getCurrentUserTenantId () {
        String tenantId = "";
        String userType = getUserType();
        if (!ADMIN_TYPE.equals(userType)){
            tenantId = userHolder.getCurrentUserTenantId();
        }
        return tenantId;
    }

    protected String getCurrentTenantId () {
        return userHolder.getCurrentUserTenantId();
    }

    protected AuthUserInfo getCurrentUser () {
        return userHolder.getCurrentUser();
    }

    protected String getUserType () {
        return userHolder.getCurrentUserType();
    }


    @Autowired
    public void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }
}
