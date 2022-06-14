package com.aibaixun.uaa.auth.filter;

import com.aibaixun.basic.entity.AuthUserInfo;
import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.common.redis.util.RedisRepository;
import com.aibaixun.uaa.auth.SecurityConstants;
import com.aibaixun.uaa.service.IAuthUserService;
import com.aibaixun.uaa.utils.CustomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.aibaixun.uaa.auth.SecurityConstants.AUTH_URL;
import static com.aibaixun.uaa.auth.SecurityConstants.SWAGGER_URL;


/**
 * @author wangxiao
 * 每次请求都经过的处理器 没认证就放过 认证了 就开始鉴权
 */
public class OncePerRequestFilter extends BasicAuthenticationFilter {

    RedisRepository redisRepository;

    IAuthUserService userService;

    public OncePerRequestFilter(AuthenticationManager authenticationManager, RedisRepository redisRepository, IAuthUserService userService) {
        super(authenticationManager);
        this.redisRepository=redisRepository;
        this.userService=userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getRequestURI().startsWith(SWAGGER_URL)){
            return;
        }
        if (request.getRequestURI().startsWith(AUTH_URL)){
            super.doFilterInternal(request,response,chain);
        }
        String token=request.getHeader(SecurityConstants.TOKEN_FIELD);
        if(StringUtils.isEmpty(token)){
            chain.doFilter(request, response);
        }
        Object o = redisRepository.get(SecurityConstants.TOKEN_PREFIX + token);
        if (o==null){
            CustomUtils.sendJsonMessage(response,JsonResult.failed("凭证已过期!"));
        }
        AuthUserInfo authUser = (AuthUserInfo)o;
        try{
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(authUser,token);
            response.setHeader(SecurityConstants.TOKEN_FIELD,token);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }catch (Exception e){
            CustomUtils.sendJsonMessage(response, JsonResult.failed("登录凭证异常!"));
        }
        super.doFilterInternal(request,response,chain);
    }


}
