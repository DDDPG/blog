package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户后台VO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBackVO {

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 登录方式
     */
    private Integer loginType;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * IP来源
     */
    private String ipSource;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最近登录时间
     */
    private LocalDateTime loginTime;

}
