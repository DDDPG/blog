package com.ican.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ican.entity.Article;
import com.ican.entity.SiteSetting;
import com.ican.mapper.*;
import com.ican.model.dto.AboutDTO;
import com.ican.model.dto.WebsiteDTO;
import com.ican.model.vo.*;
import com.ican.service.PictureService;
import com.ican.service.RedisService;
import com.ican.service.SiteSettingService;
import com.ican.service.UniqueViewService;
import com.ican.utils.IpUtils;
import com.ican.utils.UserAgentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.ican.constant.CommonConstant.*;
import static com.ican.constant.RedisConstant.*;
import static com.ican.enums.ArticleStatusEnum.PUBLIC;

/**
 * 网站配置业务接口实现类
 *
 * @author ican
 */
@Service
public class SiteSettingServiceImpl extends ServiceImpl<SiteSettingMapper, SiteSetting> implements SiteSettingService {

    @Autowired
    private SiteSettingMapper siteSettingMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UniqueViewService uniqueViewService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void updateSiteSetting(WebsiteDTO website) {
        // 修改网站配置
        SiteSetting siteSetting = SiteSetting.builder()
                .id(1)
                .webInfo(JSON.toJSONString(website))
                .build();
        siteSettingMapper.updateById(siteSetting);
        // 删除缓存
        redisService.deleteObject(SITE_SETTING);
    }

    @Override
    public WebsiteDTO getSiteSetting() {
        WebsiteDTO websiteDTO = redisService.getObject(SITE_SETTING);
        if (Objects.isNull(websiteDTO)) {
            // 从数据库中加载
            String config = siteSettingMapper.selectById(1).getWebInfo();
            websiteDTO = JSON.parseObject(config, WebsiteDTO.class);
            redisService.setObject(SITE_SETTING, websiteDTO);
        }
        return websiteDTO;
    }

    @Override
    public SiteHomeInfoVO getSiteHomeInfo() {
        // 文章数量
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, PUBLIC.getStatus()).eq(Article::getIsDelete, FALSE));
        // 分类数量
        Integer categoryCount = categoryMapper.selectCount(null);
        // 标签数量
        Integer tagCount = tagMapper.selectCount(null);
        // 博客访问量
        Integer count = redisService.getObject(BLOG_VIEW_COUNT);
        String viewCount = Optional.ofNullable(count).orElse(0).toString();
        // 网站配置
        WebsiteDTO websiteDTO = this.getSiteSetting();
        // 获取图片
        List<PictureVO> pictureVOList = pictureService.selectPictureList();
        return SiteHomeInfoVO.builder()
                .articleCount(articleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .viewCount(viewCount)
                .websiteDTO(websiteDTO)
                .pictureVOList(pictureVOList)
                .build();
    }

    @Override
    public SiteBackInfoVO getSiteBackInfo() {
        // 访问量
        Integer viewCount = redisService.getObject(BLOG_VIEW_COUNT);
        // 留言量
        Integer messageCount = messageMapper.selectCount(null);
        // 用户量
        Integer userCount = userMapper.selectCount(null);
        // 文章量
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getIsDelete, FALSE));
        // 一周用户量
        List<UniqueViewVO> uniqueViewList = uniqueViewService.listUniqueView();
        // 文章统计
        List<ArticleStatisticsVO> articleStatisticsList = articleMapper.selectArticleStatistics();
        // 分类数据
        List<CategoryVO> categoryVOList = categoryMapper.selectCategoryVO();
        // 标签数据
        List<TagVO> tagVOList = tagMapper.selectTagVOList();
        // 查询redis访问量前五的文章
        Map<Object, Double> articleMap = redisService.zReverseRangeWithScore(ARTICLE_VIEW_COUNT, 0, 4);
        SiteBackInfoVO siteBackInfoVO = SiteBackInfoVO.builder()
                .articleStatisticsList(articleStatisticsList)
                .tagVOList(tagVOList)
                .viewCount(viewCount)
                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(articleCount)
                .categoryVOList(categoryVOList)
                .uniqueViewVOList(uniqueViewList)
                .build();
        if (CollectionUtils.isNotEmpty(articleMap)) {
            // 查询文章排行
            List<ArticleRankVO> articleRankVOList = listArticleRank(articleMap);
            siteBackInfoVO.setArticleRankVOList(articleRankVOList);
        }
        return siteBackInfoVO;
    }

    @Override
    public void report() {
        // 获取用户ip
        String ipAddress = IpUtils.getIpAddress(request);
        Map<String, String> userAgentMap = UserAgentUtils.parseOsAndBrowser(request.getHeader("User-Agent"));
        // 获取访问设备
        String browser = userAgentMap.get("browser");
        String os = userAgentMap.get("os");
        // 生成唯一用户标识
        String uuid = ipAddress + browser + os;
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        // 判断是否访问
        if (!redisService.hasSetValue(UNIQUE_VISITOR, md5)) {
            // 统计游客地域分布
            String ipSource = IpUtils.getCityInfo(ipAddress);
            if (StringUtils.hasText(ipSource)) {
                String[] split = ipSource.split("\\|");
                if (split.length > 1) {
                    // 游客省份
                    ipSource = split[1].replaceAll(PROVINCE, "");
                    redisService.incrHash(VISITOR_ZONE, ipSource, 1L);
                }
            } else {
                redisService.incrHash(VISITOR_ZONE, UNKNOWN, 1L);
            }
            // 访问量+1
            redisService.incr(BLOG_VIEW_COUNT, 1);
            // 保存唯一标识
            redisService.setSet(UNIQUE_VISITOR, md5);
        }
    }

    @Override
    public String getAbout() {
        AboutDTO about = redisService.getObject(ABOUT);
        return Objects.nonNull(about) ? about.getAboutContent() : "";
    }

    @Override
    public void updateAbout(AboutDTO about) {
        redisService.setObject(ABOUT, about);
    }

    /**
     * 查询文章排行
     *
     * @param articleMap 文章浏览量信息
     * @return {@link List<ArticleRankVO>} 文章排行
     */
    private List<ArticleRankVO> listArticleRank(Map<Object, Double> articleMap) {
        // 提取文章id
        List<Integer> articleIdList = new ArrayList<>(articleMap.size());
        articleMap.forEach((key, value) -> articleIdList.add((Integer) key));
        // 查询文章信息
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle)
                .in(Article::getId, articleIdList));
        return articleList.stream()
                .map(article -> ArticleRankVO.builder()
                        .articleTitle(article.getArticleTitle())
                        .viewCount(articleMap.get(article.getId()).intValue())
                        .build())
                .sorted(Comparator.comparingInt(ArticleRankVO::getViewCount).reversed())
                .collect(Collectors.toList());
    }
}
