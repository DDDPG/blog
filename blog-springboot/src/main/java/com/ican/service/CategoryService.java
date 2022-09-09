package com.ican.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ican.entity.Category;
import com.ican.model.vo.CategoryBackVO;
import com.ican.model.dto.CategoryDTO;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.vo.CategoryOptionVO;
import com.ican.model.vo.CategoryVO;
import com.ican.model.vo.PageResult;

import java.util.List;

/**
 * 分类业务接口
 *
 * @author ican
 */
public interface CategoryService extends IService<Category> {
    /**
     * 查询后台分类
     *
     * @param condition 查询条件
     * @return 后台分类
     */
    PageResult<CategoryBackVO> listCategoryBackVO(ConditionDTO condition);

    /**
     * 新增或修改分类
     *
     * @param category 分类
     */
    void saveOrUpdateCategory(CategoryDTO category);

    /**
     * 根据id删除分类
     *
     * @param categoryIdList 分类id集合
     */
    void deleteCategoryByIds(List<Integer> categoryIdList);

    /**
     * 搜索文章分类
     *
     * @param condition 条件
     * @return 分类列表
     */
    List<CategoryOptionVO> searchCategoryOption(ConditionDTO condition);

    /**
     * 查看分类列表
     *
     * @return 分类列表
     */
    PageResult<CategoryVO> listCategoryVO();
}