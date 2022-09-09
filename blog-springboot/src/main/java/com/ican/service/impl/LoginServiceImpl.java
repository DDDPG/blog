package com.ican.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ican.enums.LoginTypeEnum;
import com.ican.exception.ServiceException;
import com.ican.model.dto.*;
import com.ican.service.LoginService;
import com.ican.service.TokenService;
import com.ican.strategy.context.SocialLoginStrategyContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录业务接口实现类
 *
 * @author ican
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SocialLoginStrategyContext socialLoginStrategyContext;

    @Override
    public String login(LoginDTO login) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
        Authentication authentication;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new ServiceException("密码不正确");
        } catch (UsernameNotFoundException e) {
            throw new ServiceException("用户名不存在");
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return tokenService.createToken(loginUser);
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public String weiBoLogin(WeiboLoginDTO weiBoLogin) {
        return socialLoginStrategyContext.executeLoginStrategy(JSON.toJSONString(weiBoLogin), LoginTypeEnum.WEIBO);
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public String giteeLogin(SocialLoginDTO giteeLogin) {
        return socialLoginStrategyContext.executeLoginStrategy(giteeLogin, LoginTypeEnum.GITEE);
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public String githubLogin(SocialLoginDTO githubLogin) {
        return socialLoginStrategyContext.executeLoginStrategy(githubLogin, LoginTypeEnum.GITHUB);
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public String qqLogin(QqLoginDTO qqLogin) {
        return socialLoginStrategyContext.executeLoginStrategy(JSON.toJSONString(qqLogin), LoginTypeEnum.QQ);
    }
}
