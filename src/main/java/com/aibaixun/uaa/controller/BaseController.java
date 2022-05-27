package com.aibaixun.uaa.controller;

import com.aibaixun.basic.entity.BaseAuthUser;
import com.aibaixun.uaa.support.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {

    private UserHolder userHolder;

    protected String getCurrentUserId (){
        return userHolder.getCurrentUserId();
    }

    protected String getCurrentUserTenantId () {
        String tenantId = "";
        String userType = getUserType();
        //管理用户查询全局用户
        if (!"admin".equals(userType)){
            tenantId = userHolder.getCurrentUserTenantId();
        }
        return tenantId;
    }

    protected String getCurrentTenantId () {
        return userHolder.getCurrentUserTenantId();
    }

    protected BaseAuthUser getCurrentUser () {
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
