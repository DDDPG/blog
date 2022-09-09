package com.ican.listener;

import com.ican.entity.Talk;
import com.ican.mapper.TalkMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import static com.ican.constant.CommonConstant.FALSE;
import static com.ican.constant.RedisConstant.TALK_KEY;

/**
 * 监听 Redis key 过期事件
 *
 * @author ican
 */
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private TalkMapper talkMapper;

    public RedisKeyExpirationListener(RedisMessageListenerContainer redisMessageListenerContainer) {
        super(redisMessageListenerContainer);
    }

    /**
     * 针对 redis 数据失效事件，进行数据处理
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 拿到key
        String expireKey = message.toString();
        if (expireKey.contains(TALK_KEY)) {
            Integer talkId = Integer.parseInt(expireKey.split(":")[1]);
            Talk newTalk = Talk.builder()
                    .id(talkId)
                    .isTime(FALSE)
                    .build();
            talkMapper.updateById(newTalk);
        }
        log.info("监听Redis key过期, key：{}, channel：{}", message, new String(pattern));
    }
}
