package com.ican.strategy.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ican.entity.User;
import com.ican.entity.Visitor;
import com.ican.exception.ServiceException;
import com.ican.mapper.UserMapper;
import com.ican.mapper.VisitorMapper;
import com.ican.model.dto.LoginUser;
import com.ican.model.dto.SocialLoginDTO;
import com.ican.model.vo.SocialTokenVO;
import com.ican.model.vo.SocialUserInfoVO;
import com.ican.service.TokenService;
import com.ican.service.impl.UserDetailsServiceImpl;
import com.ican.strategy.SocialLoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.ican.constant.CommonConstant.*;

/**
 * 抽象登录模板
 *
 * @author ican
 */
@Service
public abstract class AbstractLoginStrategyImpl implements SocialLoginStrategy {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private VisitorMapper visitorMapper;

    @Override
    public String login(String data) {
        LoginUser loginUser;
        // 获取第三方token信息
        SocialTokenVO socialToken = getSocialToken(data);
        // 判断是否已注册
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, socialToken.getOpenId())
                .eq(User::getLoginType, socialToken.getLoginType()));
        if (Objects.nonNull(user)) {
            // 创建登录用户身份
            loginUser = userDetailsService.createLoginUser(user);
        } else {
            // 获取第三方用户信息
            loginUser = saveLoginUser(socialToken);
        }
        if (Objects.equals(loginUser.getStatus(), FALSE)) {
            throw new ServiceException("用户已被禁用");
        }
        // 登录用户身份存入Security
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return tokenService.createToken(loginUser);
    }

    @Override
    public String login(SocialLoginDTO socialLogin) {
        // 获取token
        SocialTokenVO socialToken = getSocialToken(JSON.toJSONString(socialLogin));
        // 获取用户信息
        SocialUserInfoVO socialUserInfoVO = getSocialUserInfo(socialToken);
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, socialUserInfoVO.getId())
                .eq(User::getLoginType, socialToken.getLoginType()));
        LoginUser loginUser;
        if (Objects.nonNull(user)) {
            // 获取登录用户身份
            loginUser = userDetailsService.createLoginUser(user);
        } else {
            // 获取第三方用户信息
            loginUser = saveLoginUser(socialToken, socialUserInfoVO);
        }
        if (Objects.equals(loginUser.getStatus(), FALSE)) {
            throw new ServiceException("用户已被禁用");
        }
        // 登录用户身份存入Security
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return tokenService.createToken(loginUser);
    }

    /**
     * 获取第三方Token
     *
     * @param data data
     * @return {@link SocialTokenVO} 第三方token
     */
    public abstract SocialTokenVO getSocialToken(String data);

    /**
     * 获取第三方用户信息
     *
     * @param socialToken 第三方token
     * @return {@link SocialUserInfoVO} 第三方用户信息
     */
    public abstract SocialUserInfoVO getSocialUserInfo(SocialTokenVO socialToken);

    /**
     * 新增微博、QQ用户账号
     *
     * @param socialToken 第三方Token
     * @return {@link LoginUser} 登录用户身份权限
     */
    private LoginUser saveLoginUser(SocialTokenVO socialToken) {
        // 获取第三方用户信息
        SocialUserInfoVO socialUserInfo = getSocialUserInfo(socialToken);
        // 登录信息
        Visitor newVisitor = new Visitor();
        visitorMapper.insert(newVisitor);
        // 新增用户
        User newUser = User.builder()
                .visitorId(newVisitor.getId())
                .avatar(socialUserInfo.getAvatar())
                .nickname(socialUserInfo.getNickname())
                .username(socialToken.getOpenId())
                .password(socialToken.getAccessToken())
                .loginType(socialToken.getLoginType())
                .role(ROLE_VISITOR)
                .build();
        userMapper.insert(newUser);
        return userDetailsService.createLoginUser(newUser);
    }

    /**
     * 新增Gitee、Github用户账号
     *
     * @param socialToken 第三方Token
     * @return {@link LoginUser} 登录用户身份权限
     */
    private LoginUser saveLoginUser(SocialTokenVO socialToken, SocialUserInfoVO socialUserInfoVO) {
        // 登录信息
        Visitor newVisitor = new Visitor();
        visitorMapper.insert(newVisitor);
        // 新增用户
        User newUser = User.builder()
                .visitorId(newVisitor.getId())
                .avatar(socialUserInfoVO.getAvatar())
                .nickname(socialUserInfoVO.getNickname())
                .username(socialUserInfoVO.getId())
                .password(socialToken.getAccessToken())
                .loginType(socialToken.getLoginType())
                .role(ROLE_VISITOR)
                .build();
        userMapper.insert(newUser);
        return userDetailsService.createLoginUser(newUser);
    }

}
