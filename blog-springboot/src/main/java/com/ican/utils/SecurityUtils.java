package com.ican.utils;

import com.ican.exception.ServiceException;
import com.ican.model.dto.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.ican.enums.StatusCodeEnum.USER_ERROR;

/**
 * security 工具
 *
 * @author ican
 */
public class SecurityUtils {

    /**
     * 获取用户ID
     **/
    public static Integer getUserId() {
        try {
            return getLoginUser().getId();
        } catch (Exception e) {
            throw new ServiceException(USER_ERROR.getCode(), "获取用户ID异常");
        }
    }

    /**
     * 用户昵称
     **/
    public static String getNickName() {
        try {
            return getLoginUser().getNickname();
        } catch (Exception e) {
            throw new ServiceException(USER_ERROR.getCode(), "获取用户昵称异常");
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception ignored) {

        }
        return null;
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
