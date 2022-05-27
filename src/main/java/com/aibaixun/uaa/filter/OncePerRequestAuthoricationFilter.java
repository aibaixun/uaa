package com.aibaixun.uaa.filter;

import com.aibaixun.basic.entity.BaseAuthUser;
import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.common.redis.util.RedisRepository;
import com.aibaixun.uaa.config.SecurityConfig;
import com.aibaixun.uaa.config.SecurityConstants;
import com.aibaixun.uaa.service.IAuthUserService;
import com.aibaixun.uaa.utils.CustomUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * 自定义请求过滤器，token没有或者不正确的时候，
 * 告诉用户执行相应操作，token正确且未认真的情况下则放行请求，
 * 交由认证过滤器进行认证操作
 */

public class OncePerRequestAuthoricationFilter extends BasicAuthenticationFilter {

    RedisRepository redisRepository;

    IAuthUserService userService;

    public OncePerRequestAuthoricationFilter(AuthenticationManager authenticationManager, RedisRepository redisRepository, IAuthUserService userService) {
        super(authenticationManager);
        this.redisRepository=redisRepository;
        this.userService=userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //swagger json 导出 http://172.16.0.23:8088/v2/api-docs
        if (request.getRequestURI().startsWith("/v2/api-docs")){
            return;
        }
        if (request.getRequestURI().startsWith(SecurityConfig.authUrl)){
            super.doFilterInternal(request,response,chain);
        }
        String token=request.getHeader(SecurityConstants.TOKENFIELD);
        if(token==null || token.equals("")){
            //token为空，则返回空
            chain.doFilter(request, response);
        }
        Object o = redisRepository.get(SecurityConstants.TOKENPREFIX + token);
        if (o==null){
            CustomUtils.sendJsonMessage(response,JsonResult.failed("凭证已过期！"));
        }
        BaseAuthUser authUser = (BaseAuthUser)o;
        try{
            //todo
            //UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(authUser,null,authUser.getAuthorities());
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(authUser,null, List.of(new SimpleGrantedAuthority("admin")));
            response.setHeader(SecurityConstants.TOKENFIELD,token);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }catch (Exception e){
            CustomUtils.sendJsonMessage(response, JsonResult.failed("登录凭证异常!"));
        }
        super.doFilterInternal(request,response,chain);
    }
}
