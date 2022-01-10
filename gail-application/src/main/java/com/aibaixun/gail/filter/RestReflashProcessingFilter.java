package com.aibaixun.gail.filter;

import com.aibaixun.basic.entity.BaseAuthUser;
import com.aibaixun.common.redis.util.RedisRepository;
import com.aibaixun.common.util.JsonUtil;
import com.aibaixun.gail.config.SecurityConstants;
import com.aibaixun.gail.entity.AuthUser;
import com.aibaixun.gail.entity.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import java.util.HashMap;
import java.util.Map;

public class RestReflashProcessingFilter extends AbstractAuthenticationProcessingFilter {
    // todo
    Logger log = LoggerFactory.getLogger("权限");

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final RedisRepository redisRepository;
    public RestReflashProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler,
                                       AuthenticationFailureHandler failureHandler, RedisRepository redisRepository) {

        super(defaultProcessUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.redisRepository = redisRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            if(log.isDebugEnabled()) {
                log.debug("Authentication method not supported. Request method: " + request.getMethod());
            }
            throw new AuthenticationServiceException("不支持该认证身份方法");
        }
        String token = request.getHeader(SecurityConstants.TOKENFIELD);
        if (StringUtils.isEmpty(token)){
            throw new BadCredentialsException("token为空！");
        }
        BaseAuthUser user = (BaseAuthUser)redisRepository.get(SecurityConstants.TOKENPREFIX + token);
        if (user==null){
            throw new BadCredentialsException("token已过期！");
        }

        //redis清除token
        redisRepository.del(SecurityConstants.TOKENPREFIX + token);
        UserPrincipal principal = new UserPrincipal(UserPrincipal.Type.REFLASH,user.getUserId());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, null);
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
}
