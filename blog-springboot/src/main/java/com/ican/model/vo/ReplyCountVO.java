package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 回复数VO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyCountVO {

    /**
     * 评论id
     */
    private Integer commentId;

    /**
     * 回复数
     */
    private Integer replyCount;
}
