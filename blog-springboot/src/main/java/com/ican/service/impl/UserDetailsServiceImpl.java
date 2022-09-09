package com.ican.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ican.entity.User;
import com.ican.mapper.UserMapper;
import com.ican.model.dto.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 用户业务接口实现类
 *
 * @author ican
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询账号是否存在
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId, User::getVisitorId, User::getAvatar, User::getUsername,
                        User::getNickname, User::getPassword, User::getLoginType,
                        User::getEmail, User::getRole, User::getStatus)
                .eq(User::getUsername, username).last("limit 1"));
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        return createLoginUser(user);
    }

    /**
     * 创建用户信息
     *
     * @param user 用户
     * @return 用户信息
     */
    public LoginUser createLoginUser(User user) {
        return LoginUser.builder()
                .id(user.getId())
                .visitorId(user.getVisitorId())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .password(user.getPassword())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .loginType(user.getLoginType())
                .build();
    }
}
