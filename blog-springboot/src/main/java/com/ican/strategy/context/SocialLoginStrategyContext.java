package com.ican.strategy.context;

import com.ican.enums.LoginTypeEnum;
import com.ican.model.dto.SocialLoginDTO;
import com.ican.strategy.SocialLoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 登录策略上下文
 *
 * @author ican
 */
@Service
public class SocialLoginStrategyContext {

    @Autowired
    private Map<String, SocialLoginStrategy> socialLoginStrategyMap;

    /**
     * 执行微博、QQ登录策略
     *
     * @param data          data
     * @param loginTypeEnum 登录枚举
     * @return {@link String} Token
     */
    public String executeLoginStrategy(String data, LoginTypeEnum loginTypeEnum) {
        return socialLoginStrategyMap.get(loginTypeEnum.getStrategy()).login(data);
    }

    /**
     * 执行Github、Gitee登录策略
     *
     * @param socialLogin   社交登录
     * @param loginTypeEnum 登录枚举
     * @return {@link String} Token
     */
    public String executeLoginStrategy(SocialLoginDTO socialLogin, LoginTypeEnum loginTypeEnum) {
        return socialLoginStrategyMap.get(loginTypeEnum.getStrategy()).login(socialLogin);
    }

}
