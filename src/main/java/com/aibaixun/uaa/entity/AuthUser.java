package com.aibaixun.uaa.entity;

import com.aibaixun.basic.entity.BaseAuthUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthUser extends BaseAuthUser {
    private String password;

    public AuthUser() {}

    public AuthUser(BaseAuthUser baseAuthUser) {
        setUserId(baseAuthUser.getUserId());
        setUsername(baseAuthUser.getUsername());
        setTenantId(baseAuthUser.getTenantId());
        setRoleIds(baseAuthUser.getRoleIds());
        setTokenExpired(baseAuthUser.getTokenExpired());
        setReflashTokenExpired(baseAuthUser.getReflashTokenExpired());
    }

    public AuthUser(User user, List<Role> roles) {
        setUserId(user.getId());
        setTenantId(user.getTenantId());
        setUsername(user.getUsername());
        password = user.getPassword();
        setRoleIds(roles.stream().map(Role::getId).collect(Collectors.toSet()));
        //默认到期时间两分钟后
        setTokenExpired(0L);
        setReflashTokenExpired(0L);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new HashSet<>();
        Set<String> roleIds = getRoleIds();
        if (!CollectionUtils.isEmpty(roleIds)) {
            roleIds.parallelStream().forEach(role -> collection.add(new SimpleGrantedAuthority(role)));
        }
        return collection;
    }

    public String getPassword() {
        return password;
    }

    public BaseAuthUser getBaseAuthUser(){
        BaseAuthUser user = new BaseAuthUser();
        user.setUserId(getUserId());
        user.setUsername(getUsername());
        user.setTenantId(getTenantId());
        user.setRoleIds(getRoleIds());
        user.setTokenExpired(getTokenExpired());
        user.setReflashTokenExpired(getReflashTokenExpired());
        return user;
    }
}
