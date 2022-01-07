package com.aibaixun.gail.handle;

import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.common.redis.util.RedisRepository;
import com.aibaixun.common.util.JsonUtil;
import com.aibaixun.gail.config.SecurityConstants;
import com.aibaixun.gail.entity.AuthUser;
import com.aibaixun.gail.utils.CustomUtils;
import com.alibaba.nacos.common.utils.MD5Utils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.tomcat.util.security.MD5Encoder;
import org.bouncycastle.jcajce.provider.digest.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Component
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private RedisRepository redisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //验证成功则向redis缓存写入token，然后在响应头添加token，并向前端返回
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        //MD5
        String token = null;
        try {
            //token 生成规则：雪花算法生产id再md5加密
            token = MD5Utils.md5Hex(String.valueOf(IdWorker.getId()).getBytes());
        }catch (Exception exception){
            exception.printStackTrace();
        }

        redisRepository.setExpire(SecurityConstants.TOKENPREFIX + token, authUser, (authUser.getTokenExpired()-System.currentTimeMillis())/1000);

        //返回用户id，名称
        HashMap<String, String> user = new HashMap<>();
        user.put("id", authUser.getUserId() + "");
        user.put("username", authUser.getUsername());

        response.setHeader(SecurityConstants.TOKENFIELD, token);
        CustomUtils.sendJsonMessage(response, JsonResult.success(user));
    }
}
