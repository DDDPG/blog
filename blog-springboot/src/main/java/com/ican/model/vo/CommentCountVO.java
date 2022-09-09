package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论数量VO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCountVO {

    /**
     * 类型id
     */
    private Integer id;

    /**
     * 评论数量
     */
    private Integer commentCount;
}
