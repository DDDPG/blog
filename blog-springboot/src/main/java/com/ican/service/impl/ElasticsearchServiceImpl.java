package com.ican.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ican.entity.Article;
import com.ican.mapper.ArticleMapper;
import com.ican.model.vo.ArticleSearchVO;
import com.ican.service.ElasticsearchService;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * es文章业务接口实现类
 *
 * @author ican
 */
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public void saveOrUpdateArticle(Integer id) {
        try {
            Article article = articleMapper.selectById(id);
            ArticleSearchVO articleSearchVO = new ArticleSearchVO(article);
            String json = JSON.toJSONString(articleSearchVO);
            IndexRequest request = new IndexRequest("article").id(articleSearchVO.getId().toString());
            request.source(json, XContentType.JSON);
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteArticle(Integer id) {
        try {
            DeleteRequest request = new DeleteRequest("article", id.toString());
            // 2.发送请求
            client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
