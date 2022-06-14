package com.aibaixun.uaa.auth.handle;

import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.uaa.utils.CustomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangxiao
 * 认证失败 逻辑处理
 */
@Component
public class RestAuthenticationFailureHandler  implements AuthenticationFailureHandler {

    private final Logger logger = LoggerFactory.getLogger(RestAuthenticationFailureHandler.class);
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        String message = exception.getMessage();
        logger.warn("onAuthenticationFailure,message is {}",message);
        CustomUtils.sendJsonMessage(response, JsonResult.failed("认证失败,"+message));
    }
}
