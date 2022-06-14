package com.aibaixun.uaa.auth.filter;

import com.aibaixun.basic.entity.AuthUserInfo;
import com.aibaixun.common.redis.util.RedisRepository;
import com.aibaixun.uaa.auth.SecurityConstants;
import com.aibaixun.uaa.auth.UserPrincipal;
import com.aibaixun.uaa.auth.token.UaaAuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 刷新 token
 * @author wangxiao
 */
public class RestRefreshProcessingFilter extends AbstractAuthenticationProcessingFilter {


    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final RedisRepository redisRepository;
    public RestRefreshProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler,
                                       AuthenticationFailureHandler failureHandler, RedisRepository redisRepository) {

        super(defaultProcessUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.redisRepository = redisRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new AuthenticationServiceException("不支持该认证身份方法");
        }
        String token = request.getHeader(SecurityConstants.TOKEN_FIELD);
        if (StringUtils.isEmpty(token)){
            throw new BadCredentialsException("token为空！");
        }
        AuthUserInfo user = (AuthUserInfo) redisRepository.get(SecurityConstants.TOKEN_PREFIX + token);
        if (user==null){
            throw new BadCredentialsException("token已过期！");
        }
        redisRepository.del(SecurityConstants.TOKEN_PREFIX + token);
        UserPrincipal principal = new UserPrincipal(UserPrincipal.Type.REFRESH,user.getUserId());
        UaaAuthenticationToken authenticationToken = new UaaAuthenticationToken(principal, null);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
