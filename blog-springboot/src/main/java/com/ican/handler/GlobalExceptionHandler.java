package com.ican.handler;

import com.ican.exception.ServiceException;
import com.ican.model.vo.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static com.ican.enums.StatusCodeEnum.*;

/**
 * 全局异常处理
 *
 * @author ican
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(value = ServiceException.class)
    public Result<?> handleServiceException(ServiceException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常（以json方式提交）
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Result.fail(VALID_ERROR.getCode(), Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> handleSystemException(Exception e) {
        return Result.fail(SYSTEM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 处理未知异常
     */
    @ExceptionHandler(value = RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        return Result.fail(FAIL.getCode(), e.getMessage());
    }
}