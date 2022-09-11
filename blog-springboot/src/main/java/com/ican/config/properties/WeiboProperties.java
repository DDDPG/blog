package com.ican.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微博配置属性
 *
 * @author ican
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "oauth.weibo")
public class WeiboProperties {

    /**
     * 微博clientId
     */
    private String clientId;

    /**
     * 微博clientSecret
     */
    private String clientSecret;

    /**
     * 微博登录类型
     */
    private String grantType;

    /**
     * 微博回调域名
     */
    private String redirectUrl;

    /**
     * 微博访问令牌地址
     */
    private String accessTokenUrl;

    /**
     * 微博用户信息地址
     */
    private String userInfoUrl;
}
