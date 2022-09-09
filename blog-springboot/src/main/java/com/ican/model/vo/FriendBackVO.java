package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 友链后台VO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendBackVO {

    /**
     * 友链id
     */
    private Integer id;

    /**
     * 友链名称
     */
    private String color;

    /**
     * 友链名称
     */
    private String name;

    /**
     * 友链头像
     */
    private String avatar;

    /**
     * 友链地址
     */
    private String url;

    /**
     * 友链介绍
     */
    private String introduction;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
