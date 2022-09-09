package com.ican.handler;

import com.alibaba.fastjson2.JSON;
import com.ican.model.vo.Result;
import com.ican.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.ican.enums.StatusCodeEnum.UNAUTHORIZED;

/**
 * 权限不足处理
 *
 * @author ican
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        WebUtils.renderString(response, JSON.toJSONString(Result.fail(UNAUTHORIZED.getCode(),"权限不足")));
    }
}