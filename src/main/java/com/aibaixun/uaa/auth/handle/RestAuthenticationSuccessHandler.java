package com.aibaixun.uaa.auth.handle;

import com.aibaixun.basic.jwt.JwtUtil;
import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.common.redis.util.RedisRepository;
import com.aibaixun.uaa.auth.SecurityConstants;
import com.aibaixun.uaa.entity.AuthUser;
import com.aibaixun.uaa.utils.CustomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

import static com.aibaixun.uaa.auth.SecurityConstants.REFRESH_AUTH_URL;

/**
 * @author wangxiao
 * 认证失败 逻辑处理
 */
@Component
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedisRepository redisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        AuthUser authUser = (AuthUser) authentication.getPrincipal();

        String token = null;
        try {
            token = JwtUtil.encode(null,(authUser.getTokenExpired()-System.currentTimeMillis())/1000,JwtUtil.DEFAULT_USER_ID,authUser.getUserId(),JwtUtil.DEFAULT_TENANT_ID,authUser.getTenantId());
        }catch (Exception exception){
            exception.printStackTrace();
        }
        redisRepository.setExpire(SecurityConstants.TOKEN_PREFIX + authUser.getUserId(), authUser.getBaseAuthUser(), (authUser.getTokenExpired()-System.currentTimeMillis())/1000);
        HashMap<String, String> user = new HashMap<>(8);
        user.put("id", authUser.getUserId() + "");
        user.put("username", authUser.getUsername());

        response.setHeader(SecurityConstants.TOKEN_FIELD, token);
        if (request.getRequestURI().startsWith(REFRESH_AUTH_URL)){
            HashMap<String,String> newToken = new HashMap<>(4);
            newToken.put("newToken",token);
            CustomUtils.sendJsonMessage(response, JsonResult.success(newToken));
        }else {
            CustomUtils.sendJsonMessage(response, JsonResult.success(user));
        }
    }

    @Autowired
    public void setRedisRepository(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

}
