package com.ican.service;

import com.ican.model.dto.LoginUser;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

/**
 * Token业务接口
 *
 * @author ican
 */
public interface TokenService {

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    String createToken(LoginUser loginUser);

    /**
     * 生成token
     *
     * @param subject subject参数
     * @return token
     */
    String createToken(String subject);

    /**
     * 解析token
     *
     * @param token token对象
     * @return 返回
     */
    Claims parseToken(String token);


    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    void setUserAgent(LoginUser loginUser);

    /**
     * 获取用户身份信息
     *
     * @param request request
     * @return 用户信息
     */
    LoginUser getLoginUser(HttpServletRequest request);

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser 登录信息
     */
    void verifyToken(LoginUser loginUser);

    /**
     * 刷新token有效期
     *
     * @param loginUser 登录信息
     */
    void refreshToken(LoginUser loginUser);

    /**
     * 记录用户登录信息
     *
     * @param loginUser 登录信息
     */
    void recordVisitor(LoginUser loginUser);

    /**
     * 删除用户身份信息
     *
     * @param userId 用户id
     */
    void delLoginUser(Integer userId);
}
