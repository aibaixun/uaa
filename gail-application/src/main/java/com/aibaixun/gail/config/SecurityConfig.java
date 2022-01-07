package com.aibaixun.gail.config;

import com.aibaixun.common.redis.util.RedisRepository;
import com.aibaixun.gail.entity.UserPrincipal;
import com.aibaixun.gail.filter.*;
import com.aibaixun.gail.handle.*;
import com.aibaixun.gail.service.IAuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
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
    public static String reflashAuthUrl=authUrl+"/reflash";
    //public static String permissionhAuthUrl=authUrl+"/permission";
    @Autowired
    private RedisRepository redisRepository;

    //注销处理器
    @Autowired
    private AuthenticationLogout authenticationLogout;

    @Autowired
    private RestAuthenticationSuccessHandler successHandler;

    @Autowired
    private RestAuthenticationFailureHandler failureHandler;

    @Autowired
    private IAuthUserService authUserService;

    @Autowired
    private RestAuthenticationProvider restAuthenticationProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Bean
    public AuthenticationManager getManager() throws Exception {
        return super.authenticationManagerBean();
    }
    //加密
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
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
        auth.authenticationProvider(restAuthenticationProvider);
    }

    /**
     * 授权
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //权限管理
        http.authorizeRequests()
                .antMatchers("/gail/token")
                .permitAll()

                //注销处理
                .and()
                .logout()
                .logoutUrl("/gail/token/logout")
                .logoutSuccessHandler(authenticationLogout) //注销时的逻辑处理
                .permitAll()

                //开启跨域访问
                .and()
                .cors()

                //关闭csrf功能:跨站请求伪造
                .and().csrf().disable()
                .authorizeRequests()

                //需求认证授权
                .and().authorizeRequests().antMatchers("/gail/**").authenticated()
                //任何请求方式
                .anyRequest().permitAll()

                //各种认知过滤器
                .and()
                .addFilter(new OncePerRequestAuthoricationFilter(authenticationManager, redisRepository, authUserService)) //自定义请求过滤器
                .addFilterBefore(buildProcessFilter(UserPrincipal.Type.USERNAME) , UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildProcessFilter(UserPrincipal.Type.MOBILE) , UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildProcessFilter(UserPrincipal.Type.EMAIL) , UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildProcessFilter(UserPrincipal.Type.REFLASH) , UsernamePasswordAuthenticationFilter.class)
                //拦截资源获取
                //.addFilterBefore(buildProcessFilter(UserPrincipal.Type.TOKEN) , UsernamePasswordAuthenticationFilter.class)
                //.addFilter(new AuthenticationFilter(authenticationManager(), redisTemplate))   //自定义认证过滤器

                //去除默认的session、cookie
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //未登录时的逻辑处理
                .and()
                .exceptionHandling().authenticationEntryPoint(new TokenAuthenticationEntryPoint())
                //权限不足时的逻辑处理
                .accessDeniedHandler(new TokenAccessDeniedHandler());
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
        AbstractAuthenticationProcessingFilter filter = null;
        if (type==UserPrincipal.Type.USERNAME){
            filter = new RestUserNameProcessingFilter(userNameAuthUrl, successHandler, failureHandler);
        }else if(type==UserPrincipal.Type.EMAIL){
            filter = new RestEmailProcessingFilter(emailAuthUrl,successHandler,failureHandler);
        }else if(type==UserPrincipal.Type.MOBILE){
            filter = new RestMobileProcessingFilter(mobileAuthUrl,successHandler,failureHandler);
        }else if(type==UserPrincipal.Type.REFLASH){
            filter = new RestReflashProcessingFilter(reflashAuthUrl,successHandler,failureHandler,redisRepository);
        }else if(type==UserPrincipal.Type.TOKEN){
            List<String> pathsToSkip = new ArrayList<>(Arrays.asList(userNameAuthUrl,mobileAuthUrl,emailAuthUrl,reflashAuthUrl));
            SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, "/gail/**");
            filter = new RestTokenProcessingFilter(matcher);
        }

        //自定义的filter 没有authenticationManager，需要自己加入
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }
}
