package com.ican.config;

import com.ican.filter.JwtAuthenticationTokenFilter;
import com.ican.handler.AccessDeniedHandlerImpl;
import com.ican.handler.AuthenticationEntryPointImpl;
import com.ican.handler.LogoutSuccessHandlerImpl;
import com.ican.handler.MyUserDetailsChecker;
import com.ican.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security配置
 *
 * @author ican
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 未登录处理
     */
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    /**
     * 权限不足处理
     */
    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    /**
     * 退出成功处理
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * 用户状态校验
     */
    @Autowired
    private MyUserDetailsChecker userDetailsChecker;

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 解决 无法直接注入 AuthenticationManager
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 身份认证接口
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /**
     * 自定义认证逻辑
     */
    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPreAuthenticationChecks(userDetailsChecker);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        // 捕获用户不存在异常
        authenticationProvider.setHideUserNotFoundExceptions(false);
        return authenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 添加Logout filter退出成功处理
        http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        http
                // 关闭csrf
                .csrf().disable()
                // 开启跨域
                .cors().and()
                // 基于token,不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                //任何 /admin 开头的路径下的请求都需要经过 jwt 验证
                .antMatchers(HttpMethod.GET, "/admin/**").hasAnyRole("admin", "visitor")
                // 发表评论
                .antMatchers(HttpMethod.POST, "/comments").hasAnyRole("admin", "visitor")
                .antMatchers("/admin/**").hasRole("admin")
                // 点赞
                .antMatchers("/articles/*/like", "/comments/*/like", "/talks/*/like").hasAnyRole("admin", "visitor")
                //其它路径全部放行
                .anyRequest().permitAll().and()
                .exceptionHandling()
                // 未登录
                .authenticationEntryPoint(authenticationEntryPoint)
                // 权限不足
                .accessDeniedHandler(accessDeniedHandler).and()
                // token过滤器
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
