package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 评论删除DTO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDeleteDTO {

    /**
     * 评论id
     */
    @NotNull(message = "id不能为空")
    private Integer id;

    /**
     * 是否为父评论 (0否 1是)
     */
    @NotNull(message = "是否为父评论不能为空")
    private Integer isParent;
}
