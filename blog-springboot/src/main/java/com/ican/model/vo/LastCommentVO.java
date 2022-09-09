package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 最新评论VO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LastCommentVO {

    /**
     * 评论id
     */
    private Integer id;

    /**
     * 类型id
     */
    private Integer typeId;

    /**
     * 评论类型
     */
    private Integer commentType;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;
}
