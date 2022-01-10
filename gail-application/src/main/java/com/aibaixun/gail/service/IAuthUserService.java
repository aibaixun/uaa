package com.aibaixun.gail.service;

import com.aibaixun.gail.entity.AuthUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IAuthUserService{
    AuthUser loadUserByUsername(String username) throws UsernameNotFoundException;

    AuthUser loadUserByMobile(String mobile) throws UsernameNotFoundException;

    AuthUser loadUserByEmail(String email) throws UsernameNotFoundException;

    AuthUser loadUserByUserId(String userId) throws UsernameNotFoundException;
}
