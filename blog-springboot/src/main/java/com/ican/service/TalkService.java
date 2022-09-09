package com.ican.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ican.entity.Talk;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.dto.TalkDTO;
import com.ican.model.vo.PageResult;
import com.ican.model.vo.TalkBackVO;
import com.ican.model.vo.TalkVO;

import java.util.List;

/**
 * 说说业务接口
 *
 * @author ican
 */
public interface TalkService extends IService<Talk> {

    /**
     * 保存或修改说说
     *
     * @param talk 说说信息
     */
    void saveOrUpdateTalk(TalkDTO talk);

    /**
     * 删除说说
     *
     * @param talkId 说说id
     */
    void deleteTalk(Integer talkId);

    /**
     * 查看后台说说
     *
     * @param condition 条件
     * @return {@link PageResult<TalkBackVO>} 说说列表
     */
    PageResult<TalkBackVO> listTalkBackVO(ConditionDTO condition);

    /**
     * 根据id查看后台说说
     *
     * @param talkId 说说id
     * @return {@link TalkBackVO} 说说信息
     */
    TalkBackVO getBackTalkById(Integer talkId);

    /**
     * 查看首页说说
     *
     * @return 首页说说
     */
    List<String> listTalkHome();

    /**
     * 查看说说列表
     *
     * @return 说说列表
     */
    PageResult<TalkVO> listTalkVO();

    /**
     * 根据id查看说说
     *
     * @param talkId 说说id
     * @return 说说
     */
    TalkVO getTalkById(Integer talkId);
}
