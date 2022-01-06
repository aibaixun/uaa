package com.aibaixun.gail.handle;

import com.aibaixun.gail.utils.CustomUtils;
import com.aibaixun.gail.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销处理器
 */
@Component
public class AuthenticationLogout implements LogoutSuccessHandler {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getHeader("token");
        if (token == null) {
            token = request.getParameter("token");
        }
        try {
            if (token == null) {
                //token为空表示未登录,注销失败
                CustomUtils.sendJsonMessage(response, JsonData.buildError("未登录，不能进行注销操作！！！"));
            } else {
                String username = stringRedisTemplate.opsForValue().get(token);
                if (username == null) {
                    //token不正确,注销失败
                    CustomUtils.sendJsonMessage(response, JsonData.buildError("登录凭证异常，注销失败！！！"));
                } else {
                    //token正确,注销成功
                    CustomUtils.sendJsonMessage(response, JsonData.buildError("注销成功"));
                    //清空token
                    stringRedisTemplate.delete(token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CustomUtils.sendJsonMessage(response, JsonData.buildError("登录过期，重新登录"));
    }

}
