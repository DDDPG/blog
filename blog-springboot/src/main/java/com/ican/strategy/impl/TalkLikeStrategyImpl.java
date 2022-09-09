package com.ican.strategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ican.entity.Talk;
import com.ican.exception.ServiceException;
import com.ican.mapper.TalkMapper;
import com.ican.service.RedisService;
import com.ican.strategy.LikeStrategy;
import com.ican.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.ican.constant.RedisConstant.*;

/**
 * 说说点赞策略
 *
 * @author ican
 */
@Service("talkLikeStrategyImpl")
public class TalkLikeStrategyImpl implements LikeStrategy {

    @Autowired
    private RedisService redisService;

    @Autowired
    private TalkMapper talkMapper;

    @Override
    public void like(Integer talkId) {
        // 判断文章是否存在或者是否进入回收站
        Talk talk = talkMapper.selectOne(new LambdaQueryWrapper<Talk>()
                .select(Talk::getId)
                .eq(Talk::getId, talkId).last("limit 1"));
        if (Objects.isNull(talk)) {
            throw new ServiceException("说说不存在");
        }
        // 用户id作为键，说说id作为值，记录用户点赞记录
        String userLikeTalkKey = USER_TALK_LIKE + SecurityUtils.getUserId();
        // 判断是否点赞
        if (redisService.hasSetValue(userLikeTalkKey, talkId)) {
            // 取消点赞则删除用户id中的说说id
            redisService.deleteSet(userLikeTalkKey, talkId);
            // 说说点赞量-1
            redisService.decrHash(TALK_LIKE_COUNT, talkId.toString(), 1L);
        } else {
            // 点赞则在用户id记录说说id
            redisService.setSet(userLikeTalkKey, talkId);
            // 说说点赞量+1
            redisService.incrHash(TALK_LIKE_COUNT, talkId.toString(), 1L);
        }
    }

}
