package com.ican.service;

import com.ican.model.dto.LoginDTO;
import com.ican.model.dto.QqLoginDTO;
import com.ican.model.dto.SocialLoginDTO;
import com.ican.model.dto.WeiboLoginDTO;

/**
 * 登录业务接口
 *
 * @author ican
 */
public interface LoginService {

    /**
     * 用户登录
     *
     * @param login 登录参数
     * @return token
     */
    String login(LoginDTO login);

    /**
     * 微博登录
     *
     * @param weiBoLogin 微博登录信息
     * @return token
     */
    String weiBoLogin(WeiboLoginDTO weiBoLogin);

    /**
     * Gitee登录
     *
     * @param giteeLogin Gitee登录信息
     * @return token
     */
    String giteeLogin(SocialLoginDTO giteeLogin);

    /**
     * Github登录
     *
     * @param githubLogin Github登录信息
     * @return token
     */
    String githubLogin(SocialLoginDTO githubLogin);

    /**
     * QQ登录
     *
     * @param qqLogin QQ登录信息
     * @return token
     */
    String qqLogin(QqLoginDTO qqLogin);
}
