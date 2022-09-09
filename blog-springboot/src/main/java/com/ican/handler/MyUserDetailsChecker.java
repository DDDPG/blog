package com.ican.handler;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

/**
 * 用户状态校验
 *
 * @author ican
 */
@Component
public class MyUserDetailsChecker implements UserDetailsChecker {

    @Override
    public void check(UserDetails user) {
        if (!user.isEnabled()) {
            throw new DisabledException("用户已被禁用");
        } else if (!user.isAccountNonLocked()) {
            throw new LockedException("账号已锁定");
        } else if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("账号已过期");
        }
    }

}
