package com.ican.service;

/**
 * es文章业务接口
 *
 * @author ican
 */
public interface ElasticsearchService {

    /**
     * 新增或修改文章
     *
     * @param id 文章id
     */
    void saveOrUpdateArticle(Integer id);

    /**
     * 删除文章
     *
     * @param id 文章id
     */
    void deleteArticle(Integer id);
}
