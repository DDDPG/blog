package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 友链DTO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendDTO {

    /**
     * 友链id
     */
    private Integer id;

    /**
     * 友链颜色
     */
    @NotBlank(message = "颜色不能为空")
    private String color;

    /**
     * 友链名称
     */
    @NotBlank(message = "链接名不能为空")
    private String name;

    /**
     * 友链头像
     */
    @NotBlank(message = "头像不能为空")
    private String avatar;

    /**
     * 友链地址
     */
    @NotBlank(message = "地址不能为空")
    private String url;

    /**
     * 友链介绍
     */
    @NotBlank(message = "介绍不能为空")
    private String introduction;
}
