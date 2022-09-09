package com.ican.controller;

import com.ican.annotation.AccessLimit;
import com.ican.annotation.OptLog;
import com.ican.enums.LikeTypeEnum;
import com.ican.model.dto.*;
import com.ican.model.vo.*;
import com.ican.service.ArticleService;
import com.ican.strategy.context.LikeStrategyContext;
import com.ican.strategy.context.UploadStrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.ican.constant.OptTypeConstant.*;
import static com.ican.enums.FilePathEnum.ARTICLE;

/**
 * 文章控制器
 *
 * @author ican
 */
@Api(tags = "文章模块")
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Autowired
    private LikeStrategyContext likeStrategyContext;

    /**
     * 查看首页文章列表
     *
     * @return {@link Result<ArticleHomeVO>}
     */
    @ApiOperation(value = "查看首页文章列表")
    @GetMapping("/articles")
    public Result<PageResult<ArticleHomeVO>> listArticleHomeVO() {
        return Result.success(articleService.listArticleHomeVO());
    }

    /**
     * 新增或修改文章
     *
     * @param article 文章信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "新增或修改文章")
    @PostMapping("/admin/articles")
    public Result<?> saveOrUpdateArticle(@Validated @RequestBody ArticleDTO article) {
        articleService.saveOrUpdateArticle(article);
        return Result.success();
    }

    /**
     * 查看后台文章列表
     *
     * @param condition 条件
     * @return {@link Result<ArticleBackVO>} 后台文章列表
     */
    @ApiOperation(value = "查看后台文章列表")
    @GetMapping("/admin/articles")
    public Result<PageResult<ArticleBackVO>> listArticleBackVO(ConditionDTO condition) {
        return Result.success(articleService.listArticleBackVO(condition));
    }

    /**
     * 删除文章
     *
     * @param articleIdList 文章id集合
     * @return {@link Result<>}
     */
    @OptLog(optType = DELETE)
    @ApiOperation(value = "物理删除文章")
    @DeleteMapping("/admin/articles")
    public Result<?> deleteArticle(@RequestBody List<Integer> articleIdList) {
        articleService.deleteArticleByIds(articleIdList);
        return Result.success();
    }

    /**
     * 逻辑删除
     *
     * @param delete 逻辑删除信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "逻辑删除文章")
    @PutMapping("/admin/articles")
    public Result<?> updateArticleDelete(@Validated @RequestBody DeleteDTO delete) {
        articleService.updateArticleDelete(delete);
        return Result.success();
    }

    /**
     * 修改文章置顶状态
     *
     * @param articleTop 置顶信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改文章置顶状态")
    @PutMapping("/admin/articles/top")
    public Result<?> updateArticleTop(@Validated @RequestBody ArticleTopDTO articleTop) {
        articleService.updateArticleTop(articleTop);
        return Result.success();
    }

    /**
     * 上传文章图片
     *
     * @param file 文件
     * @return {@link Result<String>} 文章图片地址
     */
    @ApiOperation(value = "上传文章图片")
    @PostMapping("/admin/articles/images")
    public Result<String> saveArticleImages(@RequestParam("file") MultipartFile file) {
        return Result.success(uploadStrategyContext.executeUploadStrategy(file, ARTICLE.getPath()));
    }

    /**
     * 根据id查看后台文章
     *
     * @param articleId 文章id
     * @return {@link Result<ArticleBack>} 后台文章
     */
    @ApiOperation(value = "根据id查看后台文章")
    @GetMapping("/admin/articles/{articleId}")
    public Result<ArticleBack> getArticleBackById(@PathVariable("articleId") Integer articleId) {
        return Result.success(articleService.getArticleBackById(articleId));
    }

    /**
     * 根据id查看首页文章
     *
     * @param articleId 文章id
     * @return {@link Result<ArticleVO>} 首页文章
     */
    @ApiOperation(value = "根据id查看首页文章")
    @GetMapping("/articles/{articleId}")
    public Result<ArticleVO> getArticleById(@PathVariable("articleId") Integer articleId) {
        return Result.success(articleService.getArticleById(articleId));
    }

    /**
     * 点赞文章
     *
     * @param articleId 文章id
     * @return {@link Result<>}
     */
    @ApiOperation(value = "点赞文章")
    @AccessLimit(seconds = 60, maxCount = 3)
    @PostMapping("/articles/{articleId}/like")
    public Result<?> saveArticleLike(@PathVariable("articleId") Integer articleId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.ARTICLE, articleId);
        return Result.success();
    }

    /**
     * 查询最新文章
     *
     * @return {@link List<LastArticleVO>}
     */
    @ApiOperation(value = "查询评论总数量")
    @GetMapping("/recent/articles")
    public Result<List<LastArticleVO>> listLastArticleVO() {
        return Result.success(articleService.listLastArticleVO());
    }

    /**
     * 查看文章归档
     *
     * @return {@link Result<ArchiveVO>} 文章归档列表
     */
    @ApiOperation(value = "查看文章归档")
    @GetMapping("/articles/archives")
    public Result<PageResult<ArchiveVO>> listArchiveVO() {
        return Result.success(articleService.listArchiveVO());
    }

    /**
     * 搜索文章
     *
     * @param condition 条件
     * @return {@link Result<ArticleSearchVO>} 文章列表
     */
    @ApiOperation(value = "搜索文章")
    @GetMapping("/articles/search")
    public Result<List<ArticleSearchVO>> listArticlesBySearch(ConditionDTO condition) {
        return Result.success(articleService.listArticlesBySearch(condition));
    }

    /**
     * 根据条件查询文章
     *
     * @param condition 条件
     * @return {@link Result<ArticleConditionListVO>} 文章列表
     */
    @GetMapping("/articles/condition")
    public Result<ArticleConditionListVO> listArticleByCondition(ConditionDTO condition){
        return Result.success(articleService.listArticleByCondition(condition));
    }

}
