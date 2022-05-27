package com.aibaixun.uaa.handle;

import com.aibaixun.basic.jwt.JwtUtil;
import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.common.redis.util.RedisRepository;
import com.aibaixun.uaa.config.SecurityConfig;
import com.aibaixun.uaa.config.SecurityConstants;
import com.aibaixun.uaa.entity.AuthUser;
import com.aibaixun.uaa.utils.CustomUtils;
import com.alibaba.nacos.common.utils.MD5Utils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Component
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private RedisRepository redisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //验证成功则向redis缓存写入token，然后在响应头添加token，并向前端返回
        AuthUser authUser = (AuthUser) authentication.getPrincipal();

        //MD5
        String token = null;
        try {
            token = JwtUtil.encode(null,(authUser.getTokenExpired()-System.currentTimeMillis())/1000,JwtUtil.DEFAULT_USER_ID,authUser.getUserId(),JwtUtil.DEFAULT_TENANT_ID,authUser.getTenantId());
        }catch (Exception exception){
            exception.printStackTrace();
        }
        //保存到redis
        redisRepository.setExpire(SecurityConstants.TOKENPREFIX + authUser.getUserId(), authUser.getBaseAuthUser(), (authUser.getTokenExpired()-System.currentTimeMillis())/1000);
        //返回用户id，名称
        HashMap<String, String> user = new HashMap<>();
        user.put("id", authUser.getUserId() + "");
        user.put("username", authUser.getUsername());

        response.setHeader(SecurityConstants.TOKENFIELD, token);
        if (request.getRequestURI().startsWith(SecurityConfig.reflashAuthUrl)){
            HashMap<String,String> newToken = new HashMap<>();
            newToken.put("newToken",token);
            CustomUtils.sendJsonMessage(response, JsonResult.success(newToken));
        }else {
            CustomUtils.sendJsonMessage(response, JsonResult.success(user));
        }
    }
}
