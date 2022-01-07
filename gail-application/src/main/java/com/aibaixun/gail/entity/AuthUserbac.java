package com.aibaixun.gail.entity;

import com.aibaixun.basic.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthUserbac implements UserDetails, UserInfo {
    private String userId;
    private String tenantId;
    private String username;
    private String password;
    //token到期时间
    private Long tokenExpired;
    //刷新token到期时间
    private Long reflashTokenExpired;
    private Set<String> roleIds;

    public AuthUserbac() {}
    public AuthUserbac(User user, List<Role> roles) {
        userId = user.getId();
        tenantId = user.getTenantId();
        username = user.getUsername();
        password = user.getPassword();
        roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
        //默认到期时间两分钟后
        tokenExpired = System.currentTimeMillis()+(60*1000);
        reflashTokenExpired = tokenExpired+(60*1000);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new HashSet<>();
        if (!CollectionUtils.isEmpty(roleIds)) {
            roleIds.parallelStream().forEach(role -> collection.add(new SimpleGrantedAuthority(role)));
        }
        return collection;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUserId(){
        return userId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public String getUserLabel() {
        return username;
    }

    public Long getTokenExpired() {
        return tokenExpired;
    }

    public Long getReflashTokenExpired() {
        return reflashTokenExpired;
    }

    public void setTokenExpired(Long tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public void setReflashTokenExpired(Long reflashTokenExpired) {
        this.reflashTokenExpired = reflashTokenExpired;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "userId='" + userId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", tokenExpired=" + tokenExpired +
                ", reflashTokenExpired=" + reflashTokenExpired +
                ", roleIds=" + roleIds +
                '}';
    }
}
