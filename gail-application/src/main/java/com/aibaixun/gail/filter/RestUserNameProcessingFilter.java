package com.aibaixun.gail.filter;

import com.aibaixun.gail.config.SecurityConstants;
import com.aibaixun.gail.entity.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
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
import java.util.Map;


public class RestUserNameProcessingFilter extends AbstractAuthenticationProcessingFilter {
    // todo
    Logger log = LoggerFactory.getLogger("权限");
    private ObjectMapper objectMapper = new ObjectMapper();
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    public RestUserNameProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler,
                                      AuthenticationFailureHandler failureHandler) {

        super(defaultProcessUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            if(log.isDebugEnabled()) {
                log.debug("Authentication method not supported. Request method: " + request.getMethod());
            }
            throw new AuthenticationServiceException("不支持该认证身份方法");
        }
        Map<String,String> longinUser = new HashMap<>();
        try {
            longinUser = objectMapper.readValue(request.getReader(), HashMap.class);
        } catch (Exception e) {
            throw new AuthenticationServiceException("错误请求");
        }

        if (StringUtils.isBlank(longinUser.get(SecurityConstants.USERNAMEFIELD)) || StringUtils.isBlank(longinUser.get(SecurityConstants.PASSWORDFIELD))) {
            throw new AuthenticationServiceException("必须提供用户名和密码");
        }
        UserPrincipal principal = new UserPrincipal(UserPrincipal.Type.USERNAME,longinUser.get(SecurityConstants.USERNAMEFIELD));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, longinUser.get(SecurityConstants.PASSWORDFIELD));
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
