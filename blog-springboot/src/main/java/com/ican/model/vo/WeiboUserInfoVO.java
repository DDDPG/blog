package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微博用户信息
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeiboUserInfoVO {

    /**
     * 头像
     */
    private String avatar_hd;

    /**
     * 昵称
     */
    private String screen_name;
}
