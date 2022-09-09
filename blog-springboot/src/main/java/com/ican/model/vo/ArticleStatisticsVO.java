package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章统计
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleStatisticsVO {

    /**
     * 日期
     */
    private String date;

    /**
     * 数量
     */
    private Integer count;
}
