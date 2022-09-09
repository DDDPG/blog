package com.ican.controller;

import com.ican.annotation.OptLog;
import com.ican.model.vo.*;
import com.ican.model.dto.CategoryDTO;
import com.ican.model.dto.ConditionDTO;
import com.ican.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.ican.constant.OptTypeConstant.DELETE;
import static com.ican.constant.OptTypeConstant.SAVE_OR_UPDATE;

/**
 * 分类控制器
 *
 * @author ican
 */
@Api(tags = "分类模块")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查看后台分类
     *
     * @param condition 查询条件
     * @return {@link Result<CategoryBackVO>} 后台分类
     */
    @ApiOperation(value = "查看后台分类")
    @GetMapping("/admin/categories")
    public Result<PageResult<CategoryBackVO>> listCategoryBackVO(ConditionDTO condition) {
        return Result.success(categoryService.listCategoryBackVO(condition));
    }

    /**
     * 新增或修改分类
     *
     * @param category 分类信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "新增或修改分类")
    @PostMapping("/admin/categories")
    public Result<?> saveOrUpdateCategory(@Validated @RequestBody CategoryDTO category) {
        categoryService.saveOrUpdateCategory(category);
        return Result.success();
    }

    /**
     * 删除分类
     *
     * @param categoryIdList 分类id集合
     * @return {@link Result<>}
     */
    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除分类")
    @DeleteMapping("/admin/categories")
    public Result<?> deleteCategory(@RequestBody List<Integer> categoryIdList) {
        categoryService.deleteCategoryByIds(categoryIdList);
        return Result.success();
    }

    /**
     * 搜索文章分类
     *
     * @param condition 条件
     * @return {@link Result<CategoryOptionVO>} 分类列表
     */
    @ApiOperation(value = "搜索文章分类")
    @GetMapping("/admin/categories/search")
    public Result<List<CategoryOptionVO>> searchCategoryOption(ConditionDTO condition) {
        return Result.success(categoryService.searchCategoryOption(condition));
    }

    /**
     * 查看分类列表
     *
     * @return {@link Result<CategoryDTO>} 分类列表
     */
    @ApiOperation(value = "查看分类列表")
    @GetMapping("/categories")
    public Result<PageResult<CategoryVO>> listCategoryVO() {
        return Result.success(categoryService.listCategoryVO());
    }

}
