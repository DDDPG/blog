package com.ican.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ican.entity.Category;
import com.ican.model.vo.CategoryBackVO;
import com.ican.model.vo.CategoryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分类mapper
 *
 * @author ican
 */
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查看后台分类列表
     *
     * @param limit   页码
     * @param size    大小
     * @param keyword 关键字
     * @return 后台分类列表
     */
    List<CategoryBackVO> selectCategoryBackVO(@Param("limit") Long limit, @Param("size") Long size, @Param("keyword") String keyword);

    /**
     * 查询分类和对应文章数量
     *
     * @return 分类列表
     */
    List<CategoryVO> selectCategoryVO();
}
