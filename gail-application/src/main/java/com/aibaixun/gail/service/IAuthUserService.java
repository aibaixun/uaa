package com.aibaixun.gail.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IAuthUserService extends UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException;

    UserDetails loadUserByEmail(String email) throws UsernameNotFoundException;

    UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException;
}
