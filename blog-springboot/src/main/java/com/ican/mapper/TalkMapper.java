package com.ican.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ican.entity.Talk;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.vo.TalkBackVO;
import com.ican.model.vo.TalkVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 说说Mapper
 *
 * @author ican
 */
@Repository
public interface TalkMapper extends BaseMapper<Talk> {

    /**
     * 查询后台说说列表
     *
     * @param limit     页码
     * @param size      大小
     * @param condition 条件
     * @return 后台说说列表
     */
    List<TalkBackVO> selectTalkBackList(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDTO condition);

    /**
     * 根据id查询后台说说
     *
     * @param talkId 说说id
     * @return 说说信息
     */
    TalkBackVO selectTalkBackById(@Param("talkId") Integer talkId);

    /**
     * 查询说说列表
     *
     * @param limit 页码
     * @param size  大小
     * @return 说说列表
     */
    List<TalkVO> selectTalkList(@Param("limit") Long limit, @Param("size") Long size);

    /**
     * 根据id查询说说
     *
     * @param talkId 说说id
     * @return 说说
     */
    TalkVO selectTalkById(@Param("talkId") Integer talkId);
}
