package com.aibaixun.gail.config;

import com.aibaixun.gail.filter.*;
import com.aibaixun.gail.handle.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static String authUrl="/gail/token";
    public static String userNameAuthUrl=authUrl+"/username";
    public static String mobileAuthUrl=authUrl+"/mobile";
    public static String emailAuthUrl=authUrl+"/email";

    //java操作redis的string类型数据的类
    @Autowired
    private RedisTemplate redisTemplate;

    //注销处理器
    @Autowired
    private AuthenticationLogout authenticationLogout;

    @Autowired
    private RestAuthenticationSuccessHandler successHandler;

    @Autowired
    private RestAuthenticationFailureHandler failureHandler;


    //加密
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserService();
    }


    /**
     * 认证
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder());
    }


    /**
     * 授权
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //权限管理
        http.authorizeRequests().antMatchers("/gail/token").permitAll()
                .and()
                //开启跨域访问
                .cors().and().
                //关闭csrf功能:跨站请求伪造,默认只能通过post方式提交logout请求
                csrf().disable()
                .authorizeRequests()
                .and().authorizeRequests().antMatchers("/gail/**").authenticated()
                //任何请求方式
                .anyRequest().permitAll()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessHandler(authenticationLogout) //注销时的逻辑处理

                .and()
                .addFilterBefore(buildProcessFilter(UserPrincipal.Type.USER_NAME) , UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildProcessFilter(UserPrincipal.Type.MOBILE) , UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildProcessFilter(UserPrincipal.Type.EMAIL) , UsernamePasswordAuthenticationFilter.class)
                //.addFilter(new AuthenticationFilter(authenticationManager(), redisTemplate))   //自定义认证过滤器
                //.addFilter(new OncePerRequestAuthoricationFilter(authenticationManager(), redisTemplate, (MyUserService) userDetailsService())) //自定义请求过滤器
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)     //去除默认的session、cookie

                .and()
                .exceptionHandling().authenticationEntryPoint(new TokenAuthenticationEntryPoint())  //未登录时的逻辑处理
                .accessDeniedHandler(new TokenAccessDeniedHandler());    //权限不足时的逻辑处理
    }


    /**
     * 用于解决跨域问题
     *
     * @return
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    private AbstractAuthenticationProcessingFilter buildProcessFilter(UserPrincipal.Type type){
        if (type==UserPrincipal.Type.USER_NAME){
            return new RestUserNameProcessingFilter(userNameAuthUrl,successHandler,failureHandler);
        }else if(type==UserPrincipal.Type.EMAIL){
            return new RestEmailProcessingFilter(emailAuthUrl,successHandler,failureHandler);
        }else if(type==UserPrincipal.Type.MOBILE){
            return new RestMobileProcessingFilter(mobileAuthUrl,successHandler,failureHandler);
        }else if(type==UserPrincipal.Type.TOKEN){
            List<String> pathsToSkip = new ArrayList<>(Arrays.asList(userNameAuthUrl,mobileAuthUrl,emailAuthUrl));
            SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, "/gail/**");
            return new RestTokenProcessingFilter(matcher,successHandler,failureHandler);
        }
        return null;
    }
}
