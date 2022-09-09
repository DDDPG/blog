package com.ican.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ican.entity.Tag;
import com.ican.model.vo.TagBackVO;
import com.ican.model.vo.TagVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 标签Mapper
 *
 * @author ican
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 查询后台标签列表
     *
     * @param limit   页码
     * @param size    大小
     * @param keyword 关键字
     * @return 后台标签列表
     */
    List<TagBackVO> selectTagBackVO(@Param("limit") Long limit, @Param("size") Long size, @Param("keyword") String keyword);

    /**
     * 根据文章id查询文章标签名称
     *
     * @param articleId 文章id
     * @return 文章标签名称
     */
    List<String> selectTagNameByArticleId(@Param("articleId") Integer articleId);

    /**
     * 查询所有标签列表
     *
     * @return 所有标签
     */
    List<TagVO> selectTagVOList();
}
