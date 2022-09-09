package com.ican.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ican.entity.Tag;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.dto.TagDTO;
import com.ican.model.vo.PageResult;
import com.ican.model.vo.TagBackVO;
import com.ican.model.vo.TagVO;

import java.util.List;

/**
 * 标签业务接口
 *
 * @author ican
 */
public interface TagService extends IService<Tag> {

    /**
     * 查询后台标签列表
     *
     * @param condition 条件
     * @return 标签列表
     */
    PageResult<TagBackVO> listTagBackVO(ConditionDTO condition);

    /**
     * 新增或修改标签
     *
     * @param tag 标签信息
     */
    void saveOrUpdateTag(TagDTO tag);

    /**
     * 根据id删除标签
     *
     * @param tagIdList 标签id集合
     */
    void deleteTagByIds(List<Integer> tagIdList);

    /**
     * 搜索文章标签
     *
     * @param condition 条件
     * @return 标签列表
     */
    List<TagVO> searchTagOption(ConditionDTO condition);

    /**
     * 查看标签列表
     *
     * @return 标签列表
     */
    PageResult<TagVO> listTagVO();
}
