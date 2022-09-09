package com.ican.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ican.entity.Article;
import com.ican.entity.Category;
import com.ican.exception.ServiceException;
import com.ican.mapper.ArticleMapper;
import com.ican.mapper.CategoryMapper;
import com.ican.model.vo.CategoryBackVO;
import com.ican.model.dto.CategoryDTO;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.vo.CategoryOptionVO;
import com.ican.model.vo.CategoryVO;
import com.ican.model.vo.PageResult;
import com.ican.service.CategoryService;
import com.ican.utils.BeanCopyUtils;
import com.ican.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 分类业务接口实现类
 *
 * @author ican
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PageResult<CategoryBackVO> listCategoryBackVO(ConditionDTO condition) {
        // 查询分类数量
        Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .like(StringUtils.hasText(condition.getKeyword()), Category::getCategoryName, condition.getKeyword())
        );
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询分类列表
        List<CategoryBackVO> categoryList = categoryMapper.selectCategoryBackVO(PageUtils.getLimit(), PageUtils.getSize(), condition.getKeyword());
        return new PageResult<>(categoryList, count);
    }

    @Override
    public void saveOrUpdateCategory(CategoryDTO category) {
        // 判断分类是否存在
        Category existCategory = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, category.getCategoryName()));
        if (Objects.nonNull(existCategory) && !existCategory.getId().equals(category.getId())) {
            throw new ServiceException("分类名已存在");
        }
        // 新分类
        Category newCategory = Category.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .build();
        // 保存分类
        this.saveOrUpdate(newCategory);
    }

    @Override
    public void deleteCategoryByIds(List<Integer> categoryIdList) {
        // 判断分类下是否有文章
        Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .in(Article::getCategoryId, categoryIdList));
        if (count > 0) {
            throw new ServiceException("删除失败，该分类下存在文章");
        }
        // 批量删除分类
        categoryMapper.deleteBatchIds(categoryIdList);
    }

    @Override
    public List<CategoryOptionVO> searchCategoryOption(ConditionDTO condition) {
        // 查询分类
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .like(StringUtils.hasText(condition.getKeyword()), Category::getCategoryName, condition.getKeyword())
                .orderByDesc(Category::getId));
        return BeanCopyUtils.copyBeanList(categoryList, CategoryOptionVO.class);
    }

    @Override
    public PageResult<CategoryVO> listCategoryVO() {
        List<CategoryVO> categoryVOList = categoryMapper.selectCategoryVO();
        // 标签数量
        Integer count = categoryMapper.selectCount(null);
        return new PageResult<>(categoryVOList, count);
    }
}
