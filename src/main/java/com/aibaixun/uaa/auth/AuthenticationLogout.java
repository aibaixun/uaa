package com.aibaixun.uaa.auth;

import com.aibaixun.basic.result.BaseResultCode;
import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.common.redis.util.RedisRepository;
import com.aibaixun.uaa.auth.SecurityConstants;
import com.aibaixun.uaa.utils.CustomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销处理器
 * @author wangxiao
 */
@Component
public class AuthenticationLogout implements LogoutSuccessHandler {


    private RedisRepository redisRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getHeader(SecurityConstants.TOKEN_FIELD);
        try {
            if (token == null) {
                CustomUtils.sendJsonMessage(response, JsonResult.failed(BaseResultCode.NO_LOGIN,"未登录，不能进行注销操作！"));
            } else {
                Object o = redisRepository.get(SecurityConstants.TOKEN_PREFIX + token);
                if (o == null) {
                    CustomUtils.sendJsonMessage(response, JsonResult.failed(BaseResultCode.BAD_PARAMS,"登录凭证异常，注销失败！"));
                } else {
                    redisRepository.del(SecurityConstants.TOKEN_PREFIX + token);
                    CustomUtils.sendJsonMessage(response, JsonResult.success("注销成功"));
                }
            }
        } catch (Exception e) {
            CustomUtils.sendJsonMessage(response, JsonResult.failed(BaseResultCode.GENERAL_ERROR,"系统注销异常！"));
            e.printStackTrace();
        }
    }

    @Autowired
    public void setRedisRepository(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }
}
