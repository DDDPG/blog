package com.ican.config;

import com.ican.interceptor.AccessLimitInterceptor;
import com.ican.interceptor.PageableInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * web mvc配置
 *
 * @author ican
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AccessLimitInterceptor accessLimitInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                // 设置允许跨域的路径
                .addMapping("/**")
                // 是否允许cookie
                .allowCredentials(true)
                // 设置允许的header属性
                .allowedHeaders("*")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 设置允许的请求方式
                .allowedMethods("*")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PageableInterceptor());
        registry.addInterceptor(accessLimitInterceptor);
    }
}