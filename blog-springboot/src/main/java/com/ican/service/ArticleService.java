package com.ican.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ican.entity.Article;
import com.ican.model.dto.*;
import com.ican.model.vo.*;

import java.util.List;

/**
 * 文章业务接口
 *
 * @author ican
 */
public interface ArticleService extends IService<Article> {

    /**
     * 新增或修改文章
     *
     * @param article 文章信息
     */
    void saveOrUpdateArticle(ArticleDTO article);

    /**
     * 修改文章置顶状态
     *
     * @param articleTop 置顶信息
     */
    void updateArticleTop(ArticleTopDTO articleTop);

    /**
     * 逻辑删除文章
     *
     * @param deleteVO 逻辑删除信息
     */
    void updateArticleDelete(DeleteDTO deleteVO);

    /**
     * 根据id删除文章
     *
     * @param articleIdList 文章id集合
     */
    void deleteArticleByIds(List<Integer> articleIdList);

    /**
     * 查看文章后台列表
     *
     * @param condition 条件
     * @return 后台文章列表
     */
    PageResult<ArticleBackVO> listArticleBackVO(ConditionDTO condition);

    /**
     * 根据id查看后台文章
     *
     * @param articleId 文章id
     * @return 后台文章
     */
    ArticleBack getArticleBackById(Integer articleId);

    /**
     * 根据id查看首页文章
     *
     * @param articleId 文章id
     * @return 首页文章
     */
    ArticleVO getArticleById(Integer articleId);

    /**
     * 查看首页文章列表
     *
     * @return 首页文章列表
     */
    PageResult<ArticleHomeVO> listArticleHomeVO();

    /**
     * 查询最新文章
     *
     * @return 最新文章列表
     */
    List<LastArticleVO> listLastArticleVO();

    /**
     * 查看文章归档
     *
     * @return 文章归档
     */
    PageResult<ArchiveVO> listArchiveVO();

    /**
     * 搜索文章
     *
     * @param condition 条件
     * @return 文章列表
     */
    List<ArticleSearchVO> listArticlesBySearch(ConditionDTO condition);

    /**
     * 根据条件查询文章
     *
     * @param condition 条件
     * @return 文章列表
     */
    ArticleConditionListVO listArticleByCondition(ConditionDTO condition);
}
