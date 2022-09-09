package com.ican.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ican.entity.User;
import com.ican.model.dto.*;
import com.ican.model.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户业务接口
 *
 * @author ican
 */
public interface UserService extends IService<User> {

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    UserInfo getUserInfo();

    /**
     * 发送验证码
     *
     * @param username 用户名
     */
    void sendCode(String username);

    /**
     * 修改管理员密码
     *
     * @param password 密码信息
     */
    void updateAdminPassword(PasswordDTO password);

    /**
     * 用户注册
     *
     * @param register 注册信息
     */
    void register(RegisterDTO register);

    /**
     * 查看在线用户列表
     *
     * @param condition 条件
     * @return 在线用户列表
     */
    PageResult<OnlineVO> listOnlineUser(ConditionDTO condition);

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     */
    void updateUserInfo(UserInfoDTO userInfo);

    /**
     * 修改用户密码
     *
     * @param user 用户密码
     */
    void updatePassword(UserDTO user);

    /**
     * 修改用户头像
     *
     * @param file 头像
     * @return 头像链接
     */
    String updateUserAvatar(MultipartFile file);

    /**
     * 修改用户邮箱
     *
     * @param email 邮箱
     */
    void updateUserEmail(EmailDTO email);

    /**
     * 修改用户状态
     *
     * @param userStatus 用户禁用信息
     */
    void updateUserStatus(UserStatusDTO userStatus);

    /**
     * 查看后台用户列表
     *
     * @param condition 条件
     * @return 用户列表
     */
    PageResult<UserBackVO> listUserBackVO(ConditionDTO condition);

    /**
     * 修改管理员信息
     *
     * @param adminInfo 管理员信息
     */
    void updateAdminInfo(AdminInfoDTO adminInfo);

    /**
     * 查看用户区域分布
     *
     * @param condition 条件
     * @return {@link List<UserZoneVO>} 用户区域分布
     */
    List<UserZoneVO> listUserZone(ConditionDTO condition);

}
