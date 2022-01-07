package com.aibaixun.gail.filter;

import com.aibaixun.gail.config.SecurityConstants;
import com.aibaixun.gail.entity.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestTokenProcessingFilter extends AbstractAuthenticationProcessingFilter {
    // todo
    Logger log = LoggerFactory.getLogger("权限");
    public RestTokenProcessingFilter( RequestMatcher matcher) {
        super(matcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
//        if (!HttpMethod.POST.name().equals(request.getMethod())) {
//            if(log.isDebugEnabled()) {
//                log.debug("Authentication method not supported. Request method: " + request.getMethod());
//            }
//            throw new AuthenticationServiceException("不支持该认证身份方法");
//        }
        String token = request.getHeader(SecurityConstants.TOKENFIELD);
        if (StringUtils.isEmpty(token)){
            throw new BadCredentialsException("token为空！");
        }
        UserPrincipal principal = new UserPrincipal(UserPrincipal.Type.TOKEN,token);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, null);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

/*    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
    }*/

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
    }
}
