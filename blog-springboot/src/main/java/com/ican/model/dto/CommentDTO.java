package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 评论DTO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    /**
     * 类型id
     */
    private Integer typeId;

    /**
     * 评论类型
     */
    @NotNull(message = "评论类型不能为空")
    private Integer commentType;

    /**
     * 父评论id
     */
    private Integer parentId;

    /**
     * 被回复评论id
     */
    private Integer replyId;

    /**
     * 被回复用户id
     */
    private Integer toUid;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    private String commentContent;

}

