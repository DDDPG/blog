package com.ican.model.vo;

import com.ican.model.dto.WebsiteDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 博客首页信息
 *
 * @author ican
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteHomeInfoVO {

    /**
     * 文章数量
     */
    private Integer articleCount;

    /**
     * 分类数量
     */
    private Integer categoryCount;

    /**
     * 标签数量
     */
    private Integer tagCount;

    /**
     * 网站访问量
     */
    private String viewCount;

    /**
     * 网站配置
     */
    private WebsiteDTO websiteDTO;

    /**
     * 轮播图列表
     */
    private List<PictureVO> pictureVOList;
}
