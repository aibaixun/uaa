/*
package com.aibaixun.gail.filter;

import com.aibaixun.gail.config.SecurityConfig;
import com.aibaixun.gail.service.IAuthUserService;
import com.aibaixun.gail.utils.CustomUtils;
import com.aibaixun.gail.utils.JsonData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

*/
/**
 * 自定义请求过滤器，token没有或者不正确的时候，
 * 告诉用户执行相应操作，token正确且未认真的情况下则放行请求，
 * 交由认证过滤器进行认证操作
 *//*

public class OncePerRequestAuthoricationFilter extends BasicAuthenticationFilter {

    StringRedisTemplate stringRedisTemplate;

    IAuthUserService userService;

    public OncePerRequestAuthoricationFilter(AuthenticationManager authenticationManager, RedisTemplate redisTemplate, MyUserService userService) {
        super(authenticationManager);
        this.stringRedisTemplate=stringRedisTemplate;
        this.userService=userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getRequestURI().startsWith(SecurityConfig.authUrl)){
            super.doFilterInternal(request,response,chain);
        }
        String token=request.getHeader("token");
        if(token==null || token.equals("")){
            //token为空，则返回空
            chain.doFilter(request, response);
        }
        String username=stringRedisTemplate.opsForValue().get(token);
        try{
            //判断token情况，给予对应的处理方案
            if(username==null){
                CustomUtils.sendJsonMessage(response, JsonData.buildError("登录凭证不正确或者超时了,请重新登录！！！"));
            }else{
                UserDetails userDetails = userService.loadUserByUsername(username);
                if(userDetails!=null){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(username,null,userDetails.getAuthorities());
                    response.setHeader("token",token);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }catch (Exception e){
            CustomUtils.sendJsonMessage(response, JsonData.buildError("登录凭证异常!!!"));
        }
        super.doFilterInternal(request,response,chain);
    }
}
*/
