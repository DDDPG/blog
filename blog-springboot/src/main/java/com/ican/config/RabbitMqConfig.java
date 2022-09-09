package com.ican.config;

import com.ican.constant.MqConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置
 *
 * @author ican
 */
@Configuration
public class RabbitMqConfig {

    /**
     * 邮件交换机
     */
    @Bean
    public FanoutExchange emailExchange() {
        return new FanoutExchange(MqConstant.EMAIL_EXCHANGE, true, false);
    }

    /**
     * 邮件发送队列
     */
    @Bean
    public Queue sendQueue() {
        return new Queue(MqConstant.EMAIL_QUEUE, true);
    }

    /**
     * 邮件发送Key
     */
    @Bean
    public Binding sendQueueBinding() {
        return BindingBuilder.bind(sendQueue()).to(emailExchange());
    }

    /**
     * 文章交换机
     */
    @Bean
    public TopicExchange articleExchange() {
        return new TopicExchange(MqConstant.ARTICLE_EXCHANGE, true, false);
    }

    /**
     * 文章新增和修改队列
     */
    @Bean
    public Queue insertQueue() {
        return new Queue(MqConstant.ARTICLE_INSERT_QUEUE, true);
    }

    /**
     * 文章删除队列
     */
    @Bean
    public Queue deleteQueue() {
        return new Queue(MqConstant.ARTICLE_DELETE_QUEUE, true);
    }

    /**
     * 文章新增和修改Key
     */
    @Bean
    public Binding insertQueueBinding() {
        return BindingBuilder.bind(insertQueue()).to(articleExchange()).with(MqConstant.ARTICLE_INSERT_KEY);
    }

    /**
     * 文章删除Key
     */
    @Bean
    public Binding deleteQueueBinding() {
        return BindingBuilder.bind(deleteQueue()).to(articleExchange()).with(MqConstant.ARTICLE_DELETE_KEY);
    }
}
