package com.aibaixun.gail.handle;

import com.aibaixun.gail.utils.CustomUtils;
import com.aibaixun.gail.utils.JsonData;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未登录时处理器
 */
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        CustomUtils.sendJsonMessage(response, JsonData.buildError("请登录！！"));
    }
}

