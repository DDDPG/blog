package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章条件VO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleConditionVO {

    /**
     * 文章id
     */
    private Integer id;

    /**
     * 文章缩略图
     */
    private String articleCover;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 评论量
     */
    private Integer commentCount;

    /**
     * 点赞量
     */
    private Integer likeCount;

    /**
     * 文章分类
     */
    private CategoryOptionVO category;

    /**
     * 文章标签
     */
    private List<TagVO> tagVOList;

    /**
     * 发表时间
     */
    private LocalDateTime createTime;

}
