package com.aibaixun.gail.filter;/*
package com.px.cloud.uc.handle;

import com.px.cloud.uc.utils.CustomUtils;
import com.px.cloud.uc.utils.JsonData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

*/
/**
 * 自定义认证过滤器，判断认证成功还是失败，并给予相对应的逻辑处理
 *//*

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    AuthenticationManager authenticationManager;

    RedisTemplate redisTemplate;


    public AuthenticationFilter(AuthenticationManager authenticationManager, RedisTemplate redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.redisTemplate = redisTemplate;
    }



    //未认证时调用此方法，判断认证是否成功，认证成功与否由authenticationManager.authenticate()去判断，我们在这里只负责传递所需要的参数即可
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));
    }

    //验证成功操作
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        */
/**
         * 验证成功则向redis缓存写入token，然后在响应头添加token，并向前端返回
         *//*

        String token = UUID.randomUUID().toString().replaceAll("-", "");  //token本质就是随机生成的字符串
        //由于不能使用@Autowired因此使用stringRedisTemplate
        redisTemplate.opsForValue().set(token, request.getParameter("username"), 60 * 10, TimeUnit.SECONDS);    //存入缓存中
        response.setHeader("token", token);  //在响应头添加token
        CustomUtils.sendJsonMessage(response, JsonData.buildSuccess(token));
    }

    //验证失败
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        */
/**
         * 验证成功则向前端返回失败原因
         *//*

        CustomUtils.sendJsonMessage(response, JsonData.buildError("账号或者密码错误"));
    }
}
*/
