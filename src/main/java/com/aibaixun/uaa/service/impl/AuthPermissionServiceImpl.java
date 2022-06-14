package com.aibaixun.uaa.service.impl;

import com.aibaixun.basic.entity.AuthUserInfo;
import com.aibaixun.uaa.entity.Permission;
import com.aibaixun.uaa.entity.User;
import com.aibaixun.uaa.service.IAuthPermissionService;
import com.aibaixun.uaa.service.IPermissionService;
import com.aibaixun.uaa.service.IRoleService;
import com.aibaixun.uaa.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("authPermissionService")
public class AuthPermissionServiceImpl implements IAuthPermissionService {
    @Autowired
    private HttpServletRequest autowiredRequest;

    @Autowired
    private IPermissionService permissionService;


    @Override
    public boolean hasPermission() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserInfo user = (AuthUserInfo)authentication.getPrincipal();
        String url = getRequestUri();
        return hasPermission(user,url);
    }

    @Override
    public boolean hasPermission(String url) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserInfo user = (AuthUserInfo)authentication.getPrincipal();
        return hasPermission(user,url);
    }


    @Override
    public boolean hasPermission(String url, String uid, String method) {
        // todo 鉴权
        return true;
    }

    public boolean hasPermission(AuthUserInfo user, String url) {
        //TODO 白名单
        Set<String> roleIds = user.getRoleIds();
        if (CollectionUtils.isEmpty(roleIds)){
            return false;
        }
        List<Permission> permissions = permissionService.listByRoleIds(new ArrayList<>(roleIds));
        for (int i = 0; i < permissions.size(); i++) {
            if(url.startsWith(permissions.get(i).getResource())){
                return true;
            }
        }
        return false;

    }

    private String getRequestUri () {
        return autowiredRequest.getMethod().toUpperCase()+":"+autowiredRequest.getRequestURI();
    }
}
