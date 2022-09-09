package com.ican.listener;

import com.ican.constant.MqConstant;
import com.ican.service.ElasticsearchService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文章监听
 *
 * @author ican
 */
@Component
public class ArticleListener {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @RabbitListener(queues = MqConstant.ARTICLE_INSERT_QUEUE)
    public void listenSaveOrUpdateArticle(Integer id){
        elasticsearchService.saveOrUpdateArticle(id);
    }

    @RabbitListener(queues = MqConstant.ARTICLE_DELETE_QUEUE)
    public void listenDeleteArticle(Integer id){
        elasticsearchService.deleteArticle(id);
    }
}
