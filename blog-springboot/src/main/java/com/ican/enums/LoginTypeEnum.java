package com.ican.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录方式枚举
 *
 * @author ican
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {

    /**
     * 邮箱
     */
    EMAIL(1, "邮箱登录",""),

    /**
     * QQ
     */
    QQ(2,"QQ登录","qqLoginStrategyImpl"),

    /**
     * 微博
     */
    WEIBO(3,"微博登录","weiboLoginStrategyImpl"),

    /**
     * Gitee
     */
    GITEE(4,"Gitee登录","giteeLoginStrategyImpl"),

    /**
     * Github
     */
    GITHUB(5,"Github登录","githubLoginStrategyImpl");

    /**
     * 登录方式
     */
    private final Integer loginType;

    /**
     * 描述
     */
    private final String description;

    /**
     * 策略
     */
    private final String strategy;
}
