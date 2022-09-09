package com.ican.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ican.entity.UniqueView;
import com.ican.model.vo.UniqueViewVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 访问量Mapper
 *
 * @author ican
 */
@Repository
public interface UniqueViewMapper extends BaseMapper<UniqueView> {

    /**
     * 获取7天用户量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户量
     */
    List<UniqueViewVO> selectUniqueView(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
