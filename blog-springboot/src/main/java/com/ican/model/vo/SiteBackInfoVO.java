package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 后台网站信息VO
 *
 * @author ican
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteBackInfoVO {

    /**
     * 访问量
     */
    private Integer viewCount;

    /**
     * 留言量
     */
    private Integer messageCount;

    /**
     * 用户量
     */
    private Integer userCount;

    /**
     * 文章量
     */
    private Integer articleCount;

    /**
     * 分类统计
     */
    private List<CategoryVO> categoryVOList;

    /**
     * 标签列表
     */
    private List<TagVO> tagVOList;

    /**
     * 文章统计列表
     */
    private List<ArticleStatisticsVO> articleStatisticsList;

    /**
     * 一周用户量列表
     */
    private List<UniqueViewVO> uniqueViewVOList;

    /**
     * 文章浏览量排行
     */
    private List<ArticleRankVO> articleRankVOList;

}
