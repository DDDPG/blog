package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文章后台
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleBack {

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
     * 文章类型 (1原创 2转载 3翻译)
     */
    private Integer articleType;

    /**
     * 是否置顶 (0否 1是)
     */
    private Integer isTop;

    /**
     * 状态 (1公开 2私密 3草稿)
     */
    private Integer status;

    /**
     * 文章分类名
     */
    private String categoryName;

    /**
     * 文章标签名
     */
    private List<String> tagNameList;
}
