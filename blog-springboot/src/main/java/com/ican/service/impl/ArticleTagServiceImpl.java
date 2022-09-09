package com.ican.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ican.entity.ArticleTag;
import com.ican.mapper.ArticleTagMapper;
import com.ican.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签业务接口实现类
 *
 * @author ican
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
}
