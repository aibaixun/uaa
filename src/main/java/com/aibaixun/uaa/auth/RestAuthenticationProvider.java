package com.aibaixun.uaa.auth;

import com.aibaixun.basic.entity.BaseAuthUser;
import com.aibaixun.common.redis.util.RedisRepository;
import com.aibaixun.uaa.config.SecurityConstants;
import com.aibaixun.uaa.entity.AuthUser;
import com.aibaixun.uaa.entity.UserPrincipal;
import com.aibaixun.uaa.service.IAuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RestAuthenticationProvider  implements AuthenticationProvider {
    @Autowired
    private IAuthUserService authUserService;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RedisRepository redisRepository;

    //认证处理，返回一个Authentication的实现类则代表认证成功，返回null则代表认证失败
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //根据类型加载用户信息并校验
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserPrincipal)) {
            throw new BadCredentialsException("用户认证失败！");
        }

        UserPrincipal userPrincipal = (UserPrincipal) principal;
        AuthUser authUser = null;
        if (userPrincipal.getType()==UserPrincipal.Type.USERNAME){
            authUser = authUserService.loadUserByUsername(userPrincipal.getValue());
        } else if (userPrincipal.getType()==UserPrincipal.Type.MOBILE){
            authUser = authUserService.loadUserByMobile(userPrincipal.getValue());
        } else if (userPrincipal.getType()==UserPrincipal.Type.EMAIL){
            authUser = authUserService.loadUserByEmail(userPrincipal.getValue());
        }else if (userPrincipal.getType()==UserPrincipal.Type.TOKEN){
            Object o = redisRepository.get(SecurityConstants.TOKENPREFIX + userPrincipal.getValue());
            if (o==null){
                throw new BadCredentialsException("令牌过期！");
            }
            authUser = new AuthUser((BaseAuthUser) o);
        }else if (userPrincipal.getType()==UserPrincipal.Type.REFLASH){
            authUser = authUserService.loadUserByUserId(userPrincipal.getValue());
        }

        // 密码校验
        if (userPrincipal.getType()==UserPrincipal.Type.USERNAME||(userPrincipal.getType()==UserPrincipal.Type.MOBILE||userPrincipal.getType()==UserPrincipal.Type.EMAIL)){
            if (!passwordEncoder.matches(userPrincipal.getValue(),authUser.getPassword())){
                throw new UsernameNotFoundException("账号或密码错误");
            }
        }
        return new UsernamePasswordAuthenticationToken(authUser,null);
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
