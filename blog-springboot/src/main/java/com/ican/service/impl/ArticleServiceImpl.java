package com.ican.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ican.entity.*;
import com.ican.exception.ServiceException;
import com.ican.mapper.*;
import com.ican.model.dto.*;
import com.ican.model.vo.*;
import com.ican.service.ArticleService;
import com.ican.service.ArticleTagService;
import com.ican.service.RedisService;
import com.ican.service.TagService;
import com.ican.strategy.context.SearchStrategyContext;
import com.ican.utils.BeanCopyUtils;
import com.ican.utils.PageUtils;
import com.ican.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ican.constant.CommonConstant.FALSE;
import static com.ican.constant.CommonConstant.TRUE;
import static com.ican.constant.MqConstant.*;
import static com.ican.constant.RedisConstant.*;
import static com.ican.enums.ArticleStatusEnum.DRAFT;
import static com.ican.enums.CommentTypeEnum.ARTICLE;

/**
 * 文章业务接口实现类
 *
 * @author ican
 */
@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SearchStrategyContext searchStrategyContext;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateArticle(ArticleDTO article) {
        // 保存文章分类
        Category category = saveArticleCategory(article);
        // 保存或修改文章
        Article newArticle = BeanCopyUtils.copyBean(article, Article.class);
        newArticle.setCategoryId(category.getId());
        newArticle.setUserId(SecurityUtils.getUserId());
        // 新增或修改文章
        this.saveOrUpdate(newArticle);
        rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE, ARTICLE_INSERT_KEY, newArticle.getId());
        // 保存文章标签
        saveArticleTag(article, newArticle.getId());
    }

    @Override
    public void updateArticleTop(ArticleTopDTO articleTop) {
        // 修改文章置顶状态
        Article newArticle = Article.builder()
                .id(articleTop.getId())
                .isTop(articleTop.getIsTop())
                .build();
        articleMapper.updateById(newArticle);
    }

    @Override
    public void updateArticleDelete(DeleteDTO delete) {
        // 批量更新文章删除状态
        List<Article> articleList = delete.getIdList().stream()
                .map(id -> {
                    Article article = Article.builder()
                            .id(id)
                            .isDelete(delete.getIsDelete())
                            .isTop(FALSE)
                            .build();
                    rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE, ARTICLE_INSERT_KEY, id);
                    return article;
                })
                .collect(Collectors.toList());
        this.updateBatchById(articleList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteArticleByIds(List<Integer> articleIdList) {
        // 删除文章标签关联表的信息
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, articleIdList)
        );
        // 删除文章
        articleMapper.deleteBatchIds(articleIdList);
        articleIdList.forEach(id -> rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE, ARTICLE_DELETE_KEY, id));
        // 所有文章点赞用户id
        List<Integer> userIdList = redisService.getKeys(USER_ARTICLE_LIKE + "*").stream()
                .map(item -> {
                    String[] split = item.split(":");
                    return Integer.parseInt(split[1]);
                })
                .collect(Collectors.toList());
        // 所有评论点赞用户id
        List<Integer> commentUserIdList = redisService.getKeys(USER_COMMENT_LIKE + "*").stream()
                .map(item -> {
                    String[] split = item.split(":");
                    return Integer.parseInt(split[1]);
                })
                .collect(Collectors.toList());
        // 删除文章相关记录
        articleIdList.forEach(articleId -> {
            // 删除文章的用户点赞记录
            userIdList.forEach(userId -> redisService.deleteSet(USER_ARTICLE_LIKE + userId, articleId));
            // 删除文章浏览量
            redisService.deleteZetScore(ARTICLE_VIEW_COUNT, articleId.toString());
            // 删除文章点赞量
            redisService.deleteHash(ARTICLE_LIKE_COUNT, articleId.toString());
            // 文章下的评论id
            List<Integer> commentIdList = commentMapper.selectCommentId(ARTICLE.getType(), articleId);
            if (CollectionUtils.isNotEmpty(commentIdList)) {
                // 删除评论
                commentMapper.deleteBatchIds(commentIdList);
                // 删除评论的点赞量
                commentIdList.forEach(id -> redisService.deleteHash(COMMENT_LIKE_COUNT, id.toString()));
                Integer[] idList = commentIdList.toArray(new Integer[0]);
                // 删除评论的用户点赞记录
                commentUserIdList.forEach(userId -> redisService.deleteSet(USER_COMMENT_LIKE + userId, idList));
            }
        });
    }

    @Override
    public PageResult<ArticleBackVO> listArticleBackVO(ConditionDTO condition) {
        // 查询文章数量
        Integer count = articleMapper.countArticleBackVO(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询文章后台信息
        List<ArticleBackVO> articleBackVOList = articleMapper.selectArticleBackVO(PageUtils.getLimit(), PageUtils.getSize(), condition);
        // 浏览量
        Map<Object, Double> viewCountMap = redisService.getZsetAllScore(ARTICLE_VIEW_COUNT);
        // 点赞量
        Map<String, Integer> likeCountMap = redisService.getHashAll(ARTICLE_LIKE_COUNT);
        // 封装文章后台信息
        articleBackVOList.forEach(item -> {
            Double viewCount = viewCountMap.get(item.getId());
            if (Objects.nonNull(viewCount)) {
                item.setViewCount(viewCount.intValue());
            }
            item.setLikeCount(likeCountMap.get(item.getId().toString()));
        });
        return new PageResult<>(articleBackVOList, count);
    }

    @Override
    public ArticleBack getArticleBackById(Integer articleId) {
        // 查询文章信息
        Article article = articleMapper.selectArticleBackById(articleId);
        // 查询文章分类名称
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getCategoryName)
                .eq(Category::getId, article.getCategoryId()));
        // 查询文章标签名称
        List<String> tagNameList = tagMapper.selectTagNameByArticleId(articleId);
        // 对象转换
        ArticleBack articleBack = BeanCopyUtils.copyBean(article, ArticleBack.class);
        articleBack.setCategoryName(category.getCategoryName());
        articleBack.setTagNameList(tagNameList);
        return articleBack;
    }

    @Override
    public ArticleVO getArticleById(Integer articleId) {
        // 查询文章信息
        ArticleVO article = articleMapper.selectArticleById(articleId);
        if (Objects.isNull(article)) {
            throw new ServiceException("文章不存在");
        }
        // 浏览量+1
        redisService.incrZet(ARTICLE_VIEW_COUNT, articleId, 1D);
        // 查询上一篇文章
        ArticlePaginationVO lastArticle = articleMapper.selectLastArticle(articleId);
        // 查询下一篇文章
        ArticlePaginationVO nextArticle = articleMapper.selectNextArticle(articleId);
        article.setLastArticle(lastArticle);
        article.setNextArticle(nextArticle);
        // 查询浏览量
        Double viewCount = redisService.getZsetScore(ARTICLE_VIEW_COUNT, articleId);
        if (Objects.nonNull(viewCount)) {
            article.setViewCount(viewCount.intValue());
        }
        // 查询点赞量
        Integer likeCount = redisService.getHash(ARTICLE_LIKE_COUNT, articleId.toString());
        article.setLikeCount(likeCount);
        return article;
    }

    @Override
    public PageResult<ArticleHomeVO> listArticleHomeVO() {
        // 查询文章数量
        Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .select(Article::getId).eq(Article::getIsDelete, FALSE).eq(Article::getStatus, TRUE));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询首页文章列表
        List<ArticleHomeVO> articleHomeVOList = articleMapper.selectArticleHomeList(PageUtils.getLimit(), PageUtils.getSize());
        return new PageResult<>(articleHomeVOList, count);
    }

    @Override
    public List<LastArticleVO> listLastArticleVO() {
        return articleMapper.selectRecentArticle();
    }

    @Override
    public PageResult<ArchiveVO> listArchiveVO() {
        Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .select(Article::getId).eq(Article::getIsDelete, FALSE).eq(Article::getStatus, TRUE));
        if (count == 0) {
            return new PageResult<>();
        }
        List<ArchiveVO> archiveVOList = articleMapper.selectArchiveList(PageUtils.getLimit(), PageUtils.getSize());
        return new PageResult<>(archiveVOList, count);
    }

    @Override
    public List<ArticleSearchVO> listArticlesBySearch(ConditionDTO condition) {
        return searchStrategyContext.executeSearchStrategy(condition.getKeyword());
    }

    @Override
    public ArticleConditionListVO listArticleByCondition(ConditionDTO condition) {
        // 查询文章
        List<ArticleConditionVO> articleConditionList = articleMapper.listArticlesByCondition(PageUtils.getLimit(), PageUtils.getSize(), condition);
        if (CollectionUtils.isEmpty(articleConditionList)) {
            return new ArticleConditionListVO();
        }
        articleConditionList.forEach(article -> {
            // 查询浏览量
            Double viewCount = redisService.getZsetScore(ARTICLE_VIEW_COUNT, article.getId());
            if (Objects.nonNull(viewCount)) {
                article.setViewCount(viewCount.intValue());
            }
            // 点赞量
            Integer likeCount = redisService.getHash(ARTICLE_LIKE_COUNT, article.getId().toString());
            article.setLikeCount(likeCount);
            Integer commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                    .eq(Comment::getCommentType, ARTICLE.getType())
                    .eq(Comment::getIsCheck, TRUE)
                    .eq(Comment::getTypeId, article.getId()));
            article.setCommentCount(commentCount);
        });

        // 搜索条件对应名(标签或分类名)
        String name;
        if (Objects.nonNull(condition.getCategoryId())) {
            name = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                    .select(Category::getCategoryName)
                    .eq(Category::getId, condition.getCategoryId())).getCategoryName();
        } else {
            name = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getTagName)
                    .eq(Tag::getId, condition.getTagId())).getTagName();
        }
        return ArticleConditionListVO.builder()
                .articleConditionVOList(articleConditionList)
                .name(name)
                .build();
    }

    /**
     * 保存文章标签
     *
     * @param article   文章信息
     * @param articleId 文章id
     */
    private void saveArticleTag(ArticleDTO article, Integer articleId) {
        // 编辑文章则删除文章标签关联表的信息
        if (Objects.nonNull(article.getId())) {
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                    .eq(ArticleTag::getArticleId, article.getId())
            );
        }
        // 标签名列表
        List<String> tagNameList = article.getTagNameList();
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 查询出已存在的标签
            List<Tag> existTagList = tagService.list(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getId, Tag::getTagName)
                    .in(Tag::getTagName, tagNameList));
            List<String> existTagNameList = existTagList.stream()
                    .map(Tag::getTagName)
                    .collect(Collectors.toList());
            List<Integer> existTagIdList = existTagList.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
            // 移除已存在的标签列表
            tagNameList.removeAll(existTagNameList);
            // 含有新标签
            if (CollectionUtils.isNotEmpty(tagNameList)) {
                // 新标签列表
                List<Tag> tagList = tagNameList.stream()
                        .map(item -> Tag.builder()
                                .tagName(item)
                                .build())
                        .collect(Collectors.toList());
                // 批量保存新标签
                tagService.saveBatch(tagList);
                // 获取新标签id列表
                List<Integer> tagIdList = tagList.stream()
                        .map(Tag::getId)
                        .collect(Collectors.toList());
                // 新标签id添加到id列表
                existTagIdList.addAll(tagIdList);
            }
            // 将所有的标签绑定到文章标签关联表
            List<ArticleTag> articleTagList = existTagIdList.stream()
                    .map(item -> ArticleTag.builder()
                            .articleId(articleId)
                            .tagId(item)
                            .build())
                    .collect(Collectors.toList());
            // 批量保存到文章标签关联表
            articleTagService.saveBatch(articleTagList);
        }
    }

    /**
     * 保存文章分类
     *
     * @param article 文章信息
     * @return 分类
     */
    private Category saveArticleCategory(ArticleDTO article) {
        // 查询分类
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, article.getCategoryName()));
        // 分类不存在且不是草稿
        if (Objects.isNull(category) && !article.getStatus().equals(DRAFT.getStatus())) {
            category = Category.builder()
                    .categoryName(article.getCategoryName())
                    .build();
            // 保存分类
            categoryMapper.insert(category);
        }
        return category;
    }

}
