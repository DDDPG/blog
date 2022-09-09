package com.ican.controller;

import com.ican.annotation.OptLog;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.dto.TagDTO;
import com.ican.model.vo.*;
import com.ican.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ican.constant.OptTypeConstant.DELETE;
import static com.ican.constant.OptTypeConstant.SAVE_OR_UPDATE;

/**
 * 标签控制器
 *
 * @author ican
 */
@Api(tags = "标签模块")
@RestController
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 查看后台标签列表
     *
     * @param condition 条件
     * @return {@link PageResult<TagBackVO>} 标签列表
     */
    @ApiOperation(value = "查看后台标签")
    @RequestMapping("/admin/tags")
    public Result<PageResult<TagBackVO>> listTagBackVO(ConditionDTO condition) {
        return Result.success(tagService.listTagBackVO(condition));
    }

    /**
     * 新增或修改标签
     *
     * @param tagDTO 标签信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "新增或修改标签")
    @PostMapping("/admin/tags")
    public Result<?> saveOrUpdateTag(@Validated @RequestBody TagDTO tagDTO) {
        tagService.saveOrUpdateTag(tagDTO);
        return Result.success();
    }

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     * @return {@link Result<>}
     */
    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除标签")
    @DeleteMapping("/admin/tags")
    public Result<?> deleteTag(@RequestBody List<Integer> tagIdList) {
        tagService.deleteTagByIds(tagIdList);
        return Result.success();
    }

    /**
     * 搜索文章分类
     *
     * @param condition 条件
     * @return {@link Result<TagVO>} 标签列表
     */
    @ApiOperation(value = "搜索文章分类")
    @GetMapping("/admin/tags/search")
    public Result<List<TagVO>> searchTagOption(ConditionDTO condition) {
        return Result.success(tagService.searchTagOption(condition));
    }

    /**
     * 查看标签列表
     *
     * @return {@link Result<TagVO>} 标签列表
     */
    @ApiOperation(value = "查看标签列表")
    @GetMapping("/tags")
    public Result<PageResult<TagVO>> listTagVO() {
        return Result.success(tagService.listTagVO());
    }

}
