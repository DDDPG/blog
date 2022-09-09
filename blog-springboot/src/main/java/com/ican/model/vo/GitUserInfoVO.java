package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Git用户信息
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitUserInfoVO {

    /**
     * 用户id
     */
    private String id;

    /**
     * 头像
     */
    private String avatar_url;

    /**
     * 昵称
     */
    private String name;

    /**
     * 登录
     */
    private String login;
}
