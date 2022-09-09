package com.ican.strategy.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ican.model.vo.ArticleSearchVO;
import com.ican.strategy.SearchStrategy;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.ican.constant.CommonConstant.*;
import static com.ican.enums.ArticleStatusEnum.PUBLIC;

/**
 * ES搜索策略
 *
 * @author ican
 */
@Log4j2
@Service("esSearchStrategyImpl")
public class EsSearchStrategyImpl implements SearchStrategy {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public List<ArticleSearchVO> searchArticle(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return new ArrayList<>();
        }
        try {
            // request
            SearchRequest request = new SearchRequest("article");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            // 关键字搜索和条件过滤
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                    .must(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("all", keyword)))
                    .must(QueryBuilders.termQuery("isDelete", FALSE))
                    .must(QueryBuilders.termQuery("status", PUBLIC.getStatus()));
            // 高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder()
                    .field("articleTitle")
                    .field("articleContent")
                    .requireFieldMatch(false)
                    .preTags(PRE_TAG)
                    .postTags(POST_TAG);
            searchSourceBuilder.query(boolQueryBuilder).highlighter(highlightBuilder);
            request.source(searchSourceBuilder);
            // 发送请求
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            // 结果解析
            return handleResponse(response);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    private List<ArticleSearchVO> handleResponse(SearchResponse response) {
        List<ArticleSearchVO> articleSearchVOList = new ArrayList<>();
        // 获取文档数组
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            // 获取json并反序列化
            ArticleSearchVO articleSearchVO = JSON.parseObject(hit.getSourceAsString(), ArticleSearchVO.class);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            // 根据字段名，获取高亮结果
            if (CollectionUtils.isNotEmpty(highlightFields)) {
                HighlightField articleContent = highlightFields.get("articleContent");
                // 取第一条结果
                if (Objects.nonNull(articleContent)) {
                    articleSearchVO.setArticleContent(articleContent.getFragments()[0].toString());
                }
                HighlightField articleTitle = highlightFields.get("articleTitle");
                // 取第一条结果
                if (Objects.nonNull(articleTitle)) {
                    articleSearchVO.setArticleTitle(articleTitle.getFragments()[0].toString());
                }
            }
            articleSearchVOList.add(articleSearchVO);
        }
        return articleSearchVOList;
    }
}
