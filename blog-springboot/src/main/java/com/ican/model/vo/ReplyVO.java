package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 回复VO
 *
 * @author ican
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyVO {

    /**
     * 评论id
     */
    private Integer id;

    /**
     * 父级评论id
     */
    private Integer parentId;

    /**
     * 评论用户id
     */
    private Integer fromUid;

    /**
     * 被评论用户id
     */
    private Integer toUid;

    /**
     * 评论用户昵称
     */
    private String fromNickname;

    /**
     * 被评论用户昵称
     */
    private String toNickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;
}
