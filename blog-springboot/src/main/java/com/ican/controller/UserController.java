package com.ican.controller;

import com.ican.annotation.AccessLimit;
import com.ican.annotation.OptLog;
import com.ican.model.dto.*;
import com.ican.model.vo.*;
import com.ican.service.RedisService;
import com.ican.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Objects;

import static com.ican.constant.OptTypeConstant.UPDATE;
import static com.ican.constant.RedisConstant.LOGIN_KEY;

/**
 * 用户控制器
 *
 * @author ican
 */
@Api(tags = "用户模块")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    /**
     * 获取用户信息
     *
     * @return {@link Result<UserInfo>}
     */
    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getUserInfo")
    public Result<UserInfo> getUserInfo() {
        UserInfo userInfo = userService.getUserInfo();
        if (Objects.isNull(userInfo)) {
            return Result.fail("凭证失效，请重新登录");
        }
        return Result.success(userInfo);
    }

    /**
     * 发送邮箱验证码
     *
     * @return {@link Result<>}
     */
    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "发送邮箱验证码")
    @GetMapping("/code")
    public Result<?> sendCode(String username) {
        userService.sendCode(username);
        return Result.success();
    }

    /**
     * 用户邮箱注册
     *
     * @param register 注册信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "用户邮箱注册")
    @PostMapping("/register")
    public Result<?> register(@Validated @RequestBody RegisterDTO register) {
        userService.register(register);
        return Result.success();
    }

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "更新用户信息")
    @PutMapping("/users/info")
    public Result<?> updateUserInfo(@Validated @RequestBody UserInfoDTO userInfo) {
        userService.updateUserInfo(userInfo);
        return Result.success();
    }

    /**
     * 修改用户密码
     *
     * @param user 用户密码
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改用户密码")
    @PutMapping("/users/password")
    public Result<?> updatePassword(@Validated @RequestBody UserDTO user) {
        userService.updatePassword(user);
        return Result.success();
    }

    /**
     * 更新用户头像
     *
     * @param file 文件
     * @return {@link Result<String>} 头像地址
     */
    @ApiOperation(value = "更新用户头像")
    @PostMapping("/users/avatar")
    public Result<String> updateUserAvatar(@RequestParam(value = "file") MultipartFile file) {
        return Result.success(userService.updateUserAvatar(file));
    }

    /**
     * 绑定用户邮箱
     *
     * @param email 邮箱信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "绑定用户邮箱")
    @PostMapping("/users/email")
    public Result<?> updateUserEmail(@Validated @RequestBody EmailDTO email) {
        userService.updateUserEmail(email);
        return Result.success();
    }

    /**
     * 修改管理员信息
     *
     * @param adminInfo 管理员信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改管理员信息")
    @PutMapping("/admin/info")
    public Result<?> updateAdminInfo(@Validated @RequestBody AdminInfoDTO adminInfo) {
        userService.updateAdminInfo(adminInfo);
        return Result.success();
    }

    /**
     * 修改管理员密码
     *
     * @param password 密码信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改管理员密码")
    @PutMapping("/admin/password")
    public Result<?> updateAdminPassword(@Validated @RequestBody PasswordDTO password) {
        userService.updateAdminPassword(password);
        return Result.success();
    }

    /**
     * 查看后台用户列表
     *
     * @param condition 条件
     * @return {@link Result<UserBackVO>} 用户后台列表
     */
    @ApiOperation(value = "查看后台用户列表")
    @GetMapping("/admin/users")
    public Result<PageResult<UserBackVO>> listUserBackVO(ConditionDTO condition) {
        return Result.success(userService.listUserBackVO(condition));
    }

    /**
     * 查看在线用户
     *
     * @param condition 条件
     * @return {@link Result<OnlineVO>} 在线用户列表
     */
    @ApiOperation(value = "查看在线用户")
    @GetMapping("/admin/online")
    public Result<PageResult<OnlineVO>> listOnlineUser(ConditionDTO condition) {
        return Result.success(userService.listOnlineUser(condition));
    }

    /**
     * 下线用户
     *
     * @param userId 用户id
     * @return {@link Result<>}
     */
    @ApiOperation(value = "下线用户")
    @DeleteMapping("/admin/online/{userId}")
    public Result<?> forceLogout(@PathVariable("userId") String userId) {
        redisService.deleteObject(LOGIN_KEY + userId);
        return Result.success();
    }

    /**
     * 修改用户状态
     *
     * @param userStatus 用户禁用信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户状态")
    @PutMapping("/admin/changeStatus")
    public Result<?> updateUserStatus(@Validated @RequestBody UserStatusDTO userStatus) {
        userService.updateUserStatus(userStatus);
        return Result.success();
    }

    /**
     * 查看用户区域分布
     *
     * @param condition 条件
     * @return {@link Result<UserZoneVO>} 用户区域分布
     */
    @ApiOperation(value = "获取用户区域分布")
    @GetMapping("/admin/users/zone")
    public Result<List<UserZoneVO>> listUserZone(ConditionDTO condition) {
        return Result.success(userService.listUserZone(condition));
    }
}
