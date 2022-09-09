package com.ican.strategy.context;

import com.ican.enums.LikeTypeEnum;
import com.ican.strategy.LikeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 点赞策略上下文
 *
 * @author ican
 */
@Service
public class LikeStrategyContext {

    @Autowired
    private Map<String, LikeStrategy> likeStrategyMap;

    public void executeLikeStrategy(LikeTypeEnum likeTypeEnum, Integer typeId) {
        likeStrategyMap.get(likeTypeEnum.getStrategy()).like(typeId);
    }
}
