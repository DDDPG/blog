package com.ican.handler;

import com.alibaba.fastjson2.JSON;
import com.ican.model.vo.Result;
import com.ican.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.ican.enums.StatusCodeEnum.UNLOGIN;

/**
 * 未登录处理
 *
 * @author ican
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        WebUtils.renderString(response, JSON.toJSONString(Result.fail(UNLOGIN.getCode(), "请先登录")));
    }
}
