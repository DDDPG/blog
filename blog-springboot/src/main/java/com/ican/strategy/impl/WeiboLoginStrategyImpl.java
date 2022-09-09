package com.ican.strategy.impl;

import com.alibaba.fastjson2.JSON;
import com.ican.config.WeiboConfigProperties;
import com.ican.enums.LoginTypeEnum;
import com.ican.exception.ServiceException;
import com.ican.model.dto.WeiboLoginDTO;
import com.ican.model.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.ican.constant.SocialLoginConstant.*;

/**
 * 微博登录策略
 *
 * @author ican
 */
@Service("weiboLoginStrategyImpl")
public class WeiboLoginStrategyImpl extends AbstractLoginStrategyImpl {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeiboConfigProperties weiboConfigProperties;

    @Override
    public SocialTokenVO getSocialToken(String data) {
        WeiboLoginDTO weiBoLoginVO = JSON.parseObject(data, WeiboLoginDTO.class);
        // 获取微博Token信息
        WeiboTokenVO weiboToken = getWeiboToken(weiBoLoginVO);
        // 返回微博Token信息
        return SocialTokenVO.builder()
                .openId(weiboToken.getUid())
                .accessToken(weiboToken.getAccess_token())
                .loginType(LoginTypeEnum.WEIBO.getLoginType())
                .build();
    }

    @Override
    public SocialUserInfoVO getSocialUserInfo(SocialTokenVO socialToken) {
        Map<String, String> dataMap = new HashMap<>(2);
        // 请求参数
        dataMap.put(UID, socialToken.getOpenId());
        dataMap.put(ACCESS_TOKEN, socialToken.getAccessToken());
        // 微博用户信息
        WeiboUserInfoVO weiboUserInfoVO = restTemplate.getForObject(weiboConfigProperties.getUserInfoUrl(), WeiboUserInfoVO.class, dataMap);
        // 返回用户信息
        return SocialUserInfoVO.builder()
                .avatar(Objects.requireNonNull(weiboUserInfoVO).getAvatar_hd())
                .nickname(weiboUserInfoVO.getScreen_name())
                .build();
    }

    /**
     * 获取微博Token
     *
     * @param weiBoLogin 微博登录code
     * @return {@link WeiboTokenVO} 微博Token
     */
    private WeiboTokenVO getWeiboToken(WeiboLoginDTO weiBoLogin) {
        // 根据code换取微博uid和accessToken
        MultiValueMap<String, String> weiboData = new LinkedMultiValueMap<>();
        // 微博Token请求参数
        weiboData.add(CLIENT_ID, weiboConfigProperties.getClientId());
        weiboData.add(CLIENT_SECRET, weiboConfigProperties.getClientSecret());
        weiboData.add(GRANT_TYPE, weiboConfigProperties.getGrantType());
        weiboData.add(REDIRECT_URI, weiboConfigProperties.getRedirectUrl());
        weiboData.add(CODE, weiBoLogin.getCode());
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(weiboData, null);
        try {
            return restTemplate.exchange(weiboConfigProperties.getAccessTokenUrl(),
                    HttpMethod.POST,
                    requestEntity,
                    WeiboTokenVO.class).getBody();
        } catch (Exception e) {
            throw new ServiceException("微博登录错误");
        }
    }

}
