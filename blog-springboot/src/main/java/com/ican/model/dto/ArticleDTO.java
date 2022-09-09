package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 文章DTO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {

    /**
     * 文章id
     */
    private Integer id;

    /**
     * 文章缩略图
     */
    @NotBlank(message = "缩略图不能为空")
    private String articleCover;

    /**
     * 文章标题
     */
    @NotBlank(message = "文章标题不能为空")
    private String articleTitle;

    /**
     * 文章内容
     */
    @NotBlank(message = "文章内容不能为空")
    private String articleContent;

    /**
     * 文章类型 (1原创 2转载 3翻译)
     */
    private Integer articleType;

    /**
     * 分类名
     */
    @NotBlank(message = "文章分类不能为空")
    private String categoryName;

    /**
     * 标签
     */
    private List<String> tagNameList;

    /**
     * 是否置顶 (0否 1是)
     */
    private Integer isTop;

    /**
     * 状态 (1公开 2私密 3草稿)
     */
    private Integer status;

}
