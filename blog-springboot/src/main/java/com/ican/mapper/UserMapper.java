package com.ican.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ican.entity.User;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.vo.UserBackVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户mapper
 *
 * @author ican
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户后台数量
     *
     * @param condition 条件
     * @return 用户数量
     */
    Integer countUser(@Param("condition") ConditionDTO condition);

    /**
     * 查询后台用户列表
     *
     * @param limit     页码
     * @param size      大小
     * @param condition 条件
     * @return 用户后台列表
     */
    List<UserBackVO> listUsers(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDTO condition);
}
