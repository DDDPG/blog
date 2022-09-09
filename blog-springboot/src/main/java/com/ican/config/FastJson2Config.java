package com.ican.config;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

/**
 * FastJson2配置
 *
 * @author ican
 */
@Configuration
public class FastJson2Config {

    @Bean
    public HttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter fastJson = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setWriterFeatures(JSONWriter.Feature.WriteMapNullValue);
        fastJson.setFastJsonConfig(fastJsonConfig);
        return fastJson;
    }
}
