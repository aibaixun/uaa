package com.aibaixun.uaa.auth.filter;

import com.aibaixun.uaa.auth.SecurityConstants;
import com.aibaixun.uaa.auth.UserPrincipal;
import com.aibaixun.uaa.auth.token.UaaAuthenticationToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
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

import static com.aibaixun.uaa.utils.CustomUtils.formatRequestBody;

/**
 * 用户名密码 身份认证
 * @author wangxiao
 */
public class RestUserNameProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    public RestUserNameProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler,
                                      AuthenticationFailureHandler failureHandler) {

        super(defaultProcessUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        HashMap<String,String> longinUser = formatRequestBody(request);
        if (StringUtils.isBlank(longinUser.get(SecurityConstants.USERNAME_FIELD)) || StringUtils.isBlank(longinUser.get(SecurityConstants.PASSWORD_FIELD))) {
            throw new AuthenticationServiceException("必须提供用户名和密码");
        }
        UserPrincipal principal = new UserPrincipal(UserPrincipal.Type.USERNAME,longinUser.get(SecurityConstants.USERNAME_FIELD));
        UaaAuthenticationToken authenticationToken = new UaaAuthenticationToken(principal, longinUser.get(SecurityConstants.PASSWORD_FIELD));
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

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
