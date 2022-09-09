package com.ican.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ican.entity.Friend;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.dto.FriendDTO;
import com.ican.model.vo.FriendBackVO;
import com.ican.model.vo.FriendVO;
import com.ican.model.vo.PageResult;
import com.ican.model.vo.Result;

import java.util.List;

/**
 * 友链业务接口
 *
 * @author ican
 */
public interface FriendService extends IService<Friend> {

    /**
     * 查看友链后台列表
     *
     * @param condition 查询条件
     * @return 后台友链
     */
    PageResult<FriendBackVO> listFriendBackVO(ConditionDTO condition);

    /**
     * 新增或修改友链
     *
     * @param friend 友链
     */
    void saveOrUpdateFriend(FriendDTO friend);

    /**
     * 查看友链列表
     *
     * @return {@link Result<FriendVO>} 友链列表
     */
    List<FriendVO> listFriendVO();
}
