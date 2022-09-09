package com.ican.strategy;

import com.ican.model.dto.SocialLoginDTO;

/**
 * 第三方登录策略
 *
 * @author ican
 */
public interface SocialLoginStrategy {

    /**
     * 微博、QQ登录
     *
     * @param data data
     * @return {@link String} Token
     */
    String login(String data);

    /**
     * Gitee、Github登录
     *
     * @param socialLogin 登录信息
     * @return {@link String} Token
     */
    String login(SocialLoginDTO socialLogin);
}
