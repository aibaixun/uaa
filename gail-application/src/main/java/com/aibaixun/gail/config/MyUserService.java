package com.aibaixun.gail.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserService implements UserDetailsService {
    //todo
    /**
     * 实现UserDetailsService接口的方法，用于获取用户个人信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //todo 从数据库查询用户信息,权限信息
        //根据用户名查找用户,

        //获取用户权限，并把其添加到GrantedAuthority中
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        // 添加权限
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("admin");
        grantedAuthorities.add(admin);
        //用户名，密码，权限
        return new User("admin","admin", grantedAuthorities);
    }
}
