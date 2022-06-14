package com.aibaixun.uaa.auth.filter;

import com.aibaixun.uaa.auth.SecurityConstants;
import com.aibaixun.uaa.auth.UserPrincipal;
import com.aibaixun.uaa.auth.token.UaaAuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * token 身份认证
 * @author wangxiao
 */
public class RestTokenProcessingFilter extends AbstractAuthenticationProcessingFilter {


    public RestTokenProcessingFilter( RequestMatcher matcher) {
        super(matcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String token = request.getHeader(SecurityConstants.TOKEN_FIELD);
        if (StringUtils.isEmpty(token)){
            throw new BadCredentialsException("token为空！");
        }
        UserPrincipal principal = new UserPrincipal(UserPrincipal.Type.TOKEN,token);
        UaaAuthenticationToken authenticationToken = new UaaAuthenticationToken(principal, null);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        SecurityContextHolder.clearContext();
    }

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
