package com.ican.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ican.entity.Picture;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.vo.PictureBackVO;
import com.ican.model.vo.PictureVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ican
 */
@Repository
public interface PictureMapper extends BaseMapper<Picture> {
    /**
     * 查询后台图片列表
     *
     * @param limit     页码
     * @param size      大小
     * @param condition 条件
     * @return 后台图片列表
     */
    List<PictureBackVO> selectPictureBackVOList(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDTO condition);

    /**
     * 查询图片列表
     *
     * @return 图片列表
     */
    List<PictureVO> selectPictureVOList();
}
