package com.aibaixun.uaa.config;

import com.aibaixun.common.redis.util.RedisRepository;
import com.aibaixun.uaa.auth.AuthenticationLogout;
import com.aibaixun.uaa.auth.UaaAuthenticationEntryPoint;
import com.aibaixun.uaa.auth.provider.UaaAuthenticationProvider;
import com.aibaixun.uaa.auth.SkipPathRequestMatcher;
import com.aibaixun.uaa.auth.filter.*;
import com.aibaixun.uaa.auth.handle.*;
import com.aibaixun.uaa.service.IAuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.aibaixun.uaa.auth.SecurityConstants.*;

/**
 * @author huanghaijiang
 * security 配置
 * update by wangxiao  修改代码格式
 * add by wangxiao 优化代码块更改 地址
 */
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UaaSecurityConfig extends WebSecurityConfigurerAdapter {


    private RedisRepository redisRepository;


    private AuthenticationLogout authenticationLogout;


    private RestAuthenticationSuccessHandler successHandler;


    private RestAuthenticationFailureHandler failureHandler;


    private IAuthUserService authUserService;


    private UaaAuthenticationProvider uaaAuthenticationProvider;



    private UaaAuthenticationEntryPoint uaaAuthenticationEntryPoint;


    private AuthAccessDeniedHandler authAccessDeniedHandler;

    @Bean
    public AuthenticationManager getManager() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * 加密
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证 provider
     * @param auth AuthenticationManagerBuilder
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(uaaAuthenticationProvider);
    }

    /**
     * 授权 配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //权限管理
        http.authorizeRequests()
                .antMatchers("/uaa/auth/check")
                .permitAll()

                .and()
                .logout()
                .logoutUrl(LOGOUT_AUTH_URL)
                .logoutSuccessHandler(authenticationLogout)
                .permitAll()

                .and()
                .cors()

                .and().csrf().disable()
                .authorizeRequests()

                .and().authorizeRequests().antMatchers("/uaa/**").access("@authPermissionService.hasPermission()")
                .anyRequest().permitAll()

                .and()
                .addFilterBefore(buildUsernamePasswordFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildMobileFilter() , UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildEmailFilter() , UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildTokenFilter() , UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(uaaAuthenticationEntryPoint)

                .accessDeniedHandler(authAccessDeniedHandler);
    }


    /**
     * 用于解决跨域问题
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }



    @Bean
    public RestUserNameProcessingFilter buildUsernamePasswordFilter(){
        return new RestUserNameProcessingFilter(USERNAME_AUTH_URL, successHandler, failureHandler);
    }

    @Bean
    public RestMobileProcessingFilter buildMobileFilter(){
        return new RestMobileProcessingFilter(MOBILE_AUTH_URL, successHandler, failureHandler);
    }

    @Bean
    public RestEmailProcessingFilter buildEmailFilter(){
        return new RestEmailProcessingFilter(MOBILE_AUTH_URL, successHandler, failureHandler);
    }

    @Bean
    public RestRefreshProcessingFilter buildRefreshFilter(){
        return new RestRefreshProcessingFilter(REFRESH_AUTH_URL, successHandler, failureHandler, redisRepository);
    }

    @Bean
    public RestTokenProcessingFilter buildTokenFilter(){
        List<String> pathsToSkip = new ArrayList<>(Arrays.asList(USERNAME_AUTH_URL,MOBILE_AUTH_URL,EMAIL_AUTH_URL,REFRESH_AUTH_URL,CHECK_AUTH_URL));
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, "/uaa/**");
        return new RestTokenProcessingFilter(matcher);
    }


    @Autowired
    public void setRedisRepository(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @Autowired
    public void setAuthenticationLogout(AuthenticationLogout authenticationLogout) {
        this.authenticationLogout = authenticationLogout;
    }




    @Autowired
    public void setAuthUserService(IAuthUserService authUserService) {
        this.authUserService = authUserService;
    }


    @Autowired
    @Lazy
    public void setUaaAuthenticationProvider(UaaAuthenticationProvider uaaAuthenticationProvider) {
        this.uaaAuthenticationProvider = uaaAuthenticationProvider;
    }





    @Autowired
    public void setUaaAuthenticationEntryPoint(UaaAuthenticationEntryPoint uaaAuthenticationEntryPoint) {
        this.uaaAuthenticationEntryPoint = uaaAuthenticationEntryPoint;
    }


    @Autowired
    public void setSuccessHandler(RestAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Autowired
    public void setFailureHandler(RestAuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Autowired
    public void setAuthAccessDeniedHandler(AuthAccessDeniedHandler authAccessDeniedHandler) {
        this.authAccessDeniedHandler = authAccessDeniedHandler;
    }
}
