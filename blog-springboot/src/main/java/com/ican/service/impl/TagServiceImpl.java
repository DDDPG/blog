package com.ican.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ican.entity.ArticleTag;
import com.ican.entity.Tag;
import com.ican.exception.ServiceException;
import com.ican.mapper.ArticleTagMapper;
import com.ican.mapper.TagMapper;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.dto.TagDTO;
import com.ican.model.vo.PageResult;
import com.ican.model.vo.TagBackVO;
import com.ican.model.vo.TagVO;
import com.ican.service.TagService;
import com.ican.utils.BeanCopyUtils;
import com.ican.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 标签业务接口实现类
 *
 * @author ican
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public PageResult<TagBackVO> listTagBackVO(ConditionDTO condition) {
        // 查询标签数量
        Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.hasText(condition.getKeyword()), Tag::getTagName, condition.getKeyword()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询标签列表
        List<TagBackVO> tagList = tagMapper.selectTagBackVO(PageUtils.getLimit(), PageUtils.getSize(), condition.getKeyword());
        return new PageResult<>(tagList, count);
    }

    @Override
    public void saveOrUpdateTag(TagDTO tag) {
        // 判断标签是否存在
        Tag existingTag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId)
                .eq(Tag::getTagName, tag.getTagName())
        );
        if (Objects.nonNull(existingTag) && !existingTag.getId().equals(tag.getId())) {
            throw new ServiceException("标签名已存在");
        }
        // 新标签
        Tag newTag = Tag.builder()
                .id(tag.getId())
                .tagName(tag.getTagName())
                .build();
        // 保存标签
        this.saveOrUpdate(newTag);
    }

    @Override
    public void deleteTagByIds(List<Integer> tagIdList) {
        // 判断标签下是否有文章
        Integer count = articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getTagId, tagIdList));
        if (count > 0) {
            throw new ServiceException("删除失败，该标签下存在文章");
        }
        // 批量删除标签
        tagMapper.deleteBatchIds(tagIdList);
    }

    @Override
    public List<TagVO> searchTagOption(ConditionDTO condition) {
        // 查询标签
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.hasText(condition.getKeyword()), Tag::getTagName, condition.getKeyword())
                .orderByDesc(Tag::getId));
        return BeanCopyUtils.copyBeanList(tagList, TagVO.class);
    }

    @Override
    public PageResult<TagVO> listTagVO() {
        List<TagVO> tagVOList = tagMapper.selectTagVOList();
        // 标签数量
        Integer count = tagMapper.selectCount(null);
        return new PageResult<>(tagVOList, count);
    }
}
