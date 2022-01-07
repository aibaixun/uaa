package com.aibaixun.gail.handle;

import com.aibaixun.basic.result.BaseResultCode;
import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.common.redis.util.RedisRepository;
import com.aibaixun.gail.config.SecurityConstants;
import com.aibaixun.gail.utils.CustomUtils;
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
 */
@Component
public class AuthenticationLogout implements LogoutSuccessHandler {

    @Autowired
    private RedisRepository redisRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getHeader(SecurityConstants.TOKENFIELD);
        try {
            if (token == null) {
                //token为空表示未登录,注销失败
                CustomUtils.sendJsonMessage(response, JsonResult.failed(BaseResultCode.NO_LOGIN,"未登录，不能进行注销操作！"));
            } else {
                Object o = redisRepository.get(SecurityConstants.TOKENPREFIX + token);
                if (o == null) {
                    //token不正确,注销失败
                    CustomUtils.sendJsonMessage(response, JsonResult.failed(BaseResultCode.BAD_PARAMS,"登录凭证异常，注销失败！"));
                } else {
                    //清空token
                    redisRepository.del(SecurityConstants.TOKENPREFIX + token);
                    //token正确,注销成功
                    CustomUtils.sendJsonMessage(response, JsonResult.success("注销成功"));
                }
            }
        } catch (Exception e) {
            CustomUtils.sendJsonMessage(response, JsonResult.failed(BaseResultCode.GENERAL_ERROR,"系统注销异常！"));
            e.printStackTrace();
        }
    }

}
