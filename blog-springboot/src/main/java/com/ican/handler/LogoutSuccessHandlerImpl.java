package com.ican.handler;

import com.alibaba.fastjson2.JSON;
import com.ican.model.dto.LoginUser;
import com.ican.model.vo.Result;
import com.ican.service.TokenService;
import com.ican.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 退出成功处理
 *
 * @author ican
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    private TokenService tokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (Objects.nonNull(loginUser)) {
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getId());
        }
        WebUtils.renderString(response, JSON.toJSONString(Result.success()));
    }
}
