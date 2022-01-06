package com.aibaixun.gail.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class RestAuthenticationProvider  implements AuthenticationProvider {
    @Autowired
    private MyUserService userService;
    //认证处理，返回一个Authentication的实现类则代表认证成功，返回null则代表认证失败
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //根据类型加载用户信息并校验
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserPrincipal)) {
            throw new BadCredentialsException("用户认证失败！");
        }

        UserPrincipal userPrincipal = (UserPrincipal) principal;

        userService.loadUserByUsername("admin");
        return null;
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
