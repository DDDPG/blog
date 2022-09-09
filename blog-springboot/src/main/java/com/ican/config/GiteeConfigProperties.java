package com.ican.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Gitee配置属性
 *
 * @author ican
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "oauth.gitee")
public class GiteeConfigProperties {

    /**
     * clientId
     */
    private String clientId;

    /**
     * clientSecret
     */
    private String clientSecret;

    /**
     * Gitee登录类型
     */
    private String grantType;

    /**
     * Gitee回调域名
     */
    private String redirectUrl;

    /**
     * Gitee访问令牌地址
     */
    private String accessTokenUrl;

    /**
     * Gitee用户信息地址
     */
    private String userInfoUrl;
}
