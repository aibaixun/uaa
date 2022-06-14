package com.aibaixun.uaa.auth.provider;

import com.aibaixun.basic.entity.AuthUserInfo;
import com.aibaixun.common.redis.util.RedisRepository;
import com.aibaixun.uaa.auth.SecurityConstants;
import com.aibaixun.uaa.auth.UserPrincipal;
import com.aibaixun.uaa.auth.token.UaaAuthenticationToken;
import com.aibaixun.uaa.entity.AuthUser;
import com.aibaixun.uaa.service.IAuthUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author wangxiao
 * 认证处理
 * 这里采用一个 provider  并没用使用 token 区分
 */
@Component
public class UaaAuthenticationProvider implements AuthenticationProvider {

    private IAuthUserService authUserService;


    private BCryptPasswordEncoder passwordEncoder;


    private RedisRepository redisRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object principal = authentication.getPrincipal();
        Object credentials = authentication.getCredentials();
        if (!(principal instanceof UserPrincipal)) {
            throw new BadCredentialsException("用户认证失败!");
        }

        UserPrincipal userPrincipal = (UserPrincipal) principal;
        AuthUser authUser = null;

        switch (userPrincipal.getType()){
            case USERNAME:
                authUser = authUserService.loadUserByUsername(userPrincipal.getValue());
                checkAuthUser(authUser);
                checkAuthPassword((String) credentials,authUser.getPassword());
                break;
            case MOBILE:
                String mobile = userPrincipal.getValue();
                String matchValue = (String)redisRepository.get(SecurityConstants.MOBILE_PREFIX+ mobile);
                authUser = authUserService.loadUserByMobile(userPrincipal.getValue());
                checkMobileCode((String) credentials,matchValue);
                break;
            case EMAIL:
                authUser = authUserService.loadUserByEmail(userPrincipal.getValue());
                checkAuthUser(authUser);
                checkAuthPassword((String) credentials,authUser.getPassword());
                break;
            case TOKEN:
                Object o = redisRepository.get(SecurityConstants.TOKEN_PREFIX + userPrincipal.getValue());
                if (o==null){
                    throw new BadCredentialsException("令牌过期!");
                }
                authUser = new AuthUser((AuthUserInfo) o);
                break;
            case REFRESH:
                authUser = authUserService.loadUserByUserId(userPrincipal.getValue());
                break;
            default:
                break;
        }

        return new UaaAuthenticationToken(authUser,authUser.getUserId(),authUser.getAuthorities());
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return (UaaAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Autowired
    public void setAuthUserService(IAuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setRedisRepository(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }


    private void checkAuthUser(AuthUserInfo authUserInfo){
        if (Objects.isNull(authUserInfo)){
            throw new BadCredentialsException("认证信息不存在");
        }
    }

    private void checkAuthPassword(String targetValue,String matchValue){
        if (Objects.isNull(targetValue)){
            throw new BadCredentialsException("账号或者密码错误，请检查输入信息");
        }
        if (!passwordEncoder.matches(targetValue,matchValue)){
            throw new UsernameNotFoundException("账号或者密码错误，请检查输入信息");
        }
    }

    private void checkMobileCode(String targetValue, String matchValue){
       if (!StringUtils.equals(targetValue,matchValue)){
           throw new UsernameNotFoundException("验证码错误请重试");
       }
    }

}
