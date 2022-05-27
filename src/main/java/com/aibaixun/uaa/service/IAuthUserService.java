package com.aibaixun.uaa.service;

import com.aibaixun.uaa.entity.AuthUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IAuthUserService{
    AuthUser loadUserByUsername(String username) throws UsernameNotFoundException;

    AuthUser loadUserByMobile(String mobile) throws UsernameNotFoundException;

    AuthUser loadUserByEmail(String email) throws UsernameNotFoundException;

    AuthUser loadUserByUserId(String userId) throws UsernameNotFoundException;
}
