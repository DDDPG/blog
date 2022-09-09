package com.ican.controller;

import com.ican.model.dto.QqLoginDTO;
import com.ican.model.dto.SocialLoginDTO;
import com.ican.model.dto.LoginDTO;
import com.ican.model.dto.WeiboLoginDTO;
import com.ican.model.vo.Result;
import com.ican.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 登录控制器
 *
 * @author ican
 */
@Api(tags = "登录模块")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登录
     *
     * @param login 登录参数
     * @return {@link Result<String>} token
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody LoginDTO login) {
        return Result.success(loginService.login(login));
    }

    /**
     * 微博登录
     *
     * @param weiBoLogin 微博登录信息
     * @return {@link Result<String>} Token
     */
    @ApiOperation(value = "微博登录")
    @PostMapping("/oauth/weibo")
    public Result<String> weiboLogin(@Validated @RequestBody WeiboLoginDTO weiBoLogin) {
        return Result.success(loginService.weiBoLogin(weiBoLogin));
    }

    /**
     * Gitee登录
     *
     * @param giteeLogin Gitee登录信息
     * @return {@link Result<String>} Token
     */
    @ApiOperation(value = "Gitee登录")
    @PostMapping("/oauth/gitee")
    public Result<String> giteeLogin(@Validated @RequestBody SocialLoginDTO giteeLogin) {
        return Result.success(loginService.giteeLogin(giteeLogin));
    }

    /**
     * Github登录
     *
     * @param githubLogin Github登录信息
     * @return {@link Result<String>} Token
     */
    @ApiOperation(value = "Github登录")
    @PostMapping("/oauth/github")
    public Result<String> githubLogin(@Validated @RequestBody SocialLoginDTO githubLogin) {
        return Result.success(loginService.githubLogin(githubLogin));
    }

    /**
     * QQ登录
     *
     * @param qqLogin QQ登录信息
     * @return {@link Result<String>} Token
     */
    @ApiOperation(value = "QQ登录")
    @PostMapping("/oauth/qq")
    public Result<String> qqLogin(@Validated @RequestBody QqLoginDTO qqLogin) {
        return Result.success(loginService.qqLogin(qqLogin));
    }

}
