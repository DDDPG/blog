package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 文章条件列表VO
 *
 * @author ican
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleConditionListVO {

    /**
     * 文章列表
     */
    private List<ArticleConditionVO> articleConditionVOList;

    /**
     * 条件名
     */
    private String name;
}
