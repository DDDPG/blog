package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章首页VO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleHomeVO {

    /**
     * 文章id
     */
    private Integer id;

    /**
     * 文章缩略图
     */
    private String articleCover;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 文章分类
     */
    private CategoryOptionVO category;

    /**
     * 文章标签
     */
    private List<TagVO> tagVOList;

    /**
     * 是否置顶 (0否 1是)
     */
    private Integer isTop;

    /**
     * 发表时间
     */
    private LocalDateTime createTime;
}
