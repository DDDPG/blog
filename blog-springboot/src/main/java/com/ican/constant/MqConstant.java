package com.ican.constant;

/**
 * MQ常量
 *
 * @author ican
 */
public class MqConstant {

    /**
     * 邮件交换机
     */
    public static final String EMAIL_EXCHANGE = "email.topic";

    /**
     * 邮件队列
     */
    public static final String EMAIL_QUEUE = "email.queue";

    /**
     * 发送邮件RoutingKey
     */
    public static final String EMAIL_SEND_KEY = "email.send";

    /**
     * 文章交换机
     */
    public static final String ARTICLE_EXCHANGE = "article.topic";

    /**
     * 文章新增和修改队列
     */
    public static final String ARTICLE_INSERT_QUEUE = "article.insert.queue";

    /**
     * 文章删除队列
     */
    public final static String ARTICLE_DELETE_QUEUE = "article.delete.queue";

    /**
     * 文章新增和修改的RoutingKey
     */
    public final static String ARTICLE_INSERT_KEY = "article.insert";

    /**
     * 文章删除的RoutingKey
     */
    public final static String ARTICLE_DELETE_KEY = "article.delete";
}
