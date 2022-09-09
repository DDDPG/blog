package com.ican.model.vo;

import com.ican.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章搜索VO
 *
 * @author ican
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSearchVO {

    /**
     * 文章id
     */
    private Integer id;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 是否删除
     */

    private Integer isDelete;

    /**
     * 文章状态
     */
    private Integer status;

    public ArticleSearchVO(Article article) {
        this.id = article.getId();
        this.articleTitle = article.getArticleTitle();
        this.articleContent = article.getArticleContent();
        this.isDelete = article.getIsDelete();
        this.status = article.getStatus();
    }
}
