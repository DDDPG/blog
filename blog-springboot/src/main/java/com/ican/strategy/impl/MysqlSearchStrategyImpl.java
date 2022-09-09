package com.ican.strategy.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ican.mapper.ArticleMapper;
import com.ican.model.vo.ArticleSearchVO;
import com.ican.strategy.SearchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ican.constant.CommonConstant.*;

/**
 * MySQL搜索策略
 *
 * @author ican
 */
@Service("mySqlSearchStrategyImpl")
public class MysqlSearchStrategyImpl implements SearchStrategy {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<ArticleSearchVO> searchArticle(String keyword) {
        // 判空
        if (StringUtils.isBlank(keyword)) {
            return new ArrayList<>();
        }
        List<ArticleSearchVO> articleSearchVOList = articleMapper.searchArticle(keyword);
        return articleSearchVOList.stream()
                .map(item -> {
                    // 获取关键词第一次出现的位置
                    String articleContent = item.getArticleContent();
                    int index = item.getArticleContent().indexOf(keyword);
                    if (index != -1) {
                        // 获取关键词前面的文字
                        int preIndex = index > 25 ? index - 25 : 0;
                        String preText = item.getArticleContent().substring(preIndex, index);
                        // 获取关键词到后面的文字
                        int last = index + keyword.length();
                        int postLength = item.getArticleContent().length() - last;
                        int postIndex = postLength > 175 ? last + 175 : last + postLength;
                        String postText = item.getArticleContent().substring(index, postIndex);
                        // 文章内容高亮
                        articleContent = (preText + postText).replaceAll(keyword, PRE_TAG + keyword + POST_TAG);
                    }
                    // 文章标题高亮
                    String articleTitle = item.getArticleTitle().replaceAll(keyword, PRE_TAG + keyword + POST_TAG);
                    return ArticleSearchVO.builder()
                            .id(item.getId())
                            .articleTitle(articleTitle)
                            .articleContent(articleContent)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
