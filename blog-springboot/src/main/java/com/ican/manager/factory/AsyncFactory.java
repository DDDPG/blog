package com.ican.manager.factory;

import com.ican.entity.ExceptionLog;
import com.ican.entity.OperationLog;
import com.ican.entity.Visitor;
import com.ican.service.ExceptionLogService;
import com.ican.service.OperationLogService;
import com.ican.service.VisitorService;
import com.ican.utils.SpringUtils;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author ican
 */
public class AsyncFactory {

    /**
     * 记录登录信息
     *
     * @param visitor 登录信息
     * @return 任务task
     */
    public static TimerTask recordVisitor(final Visitor visitor) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(VisitorService.class).updateVisitor(visitor);
            }
        };
    }

    /**
     * 记录操作日志
     *
     * @param operationLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOperation(final OperationLog operationLog) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(OperationLogService.class).saveOperationLog(operationLog);
            }
        };
    }

    /**
     * 记录异常日志
     *
     * @param exceptionLog 异常日志信息
     * @return 任务task
     */
    public static TimerTask recordException(final ExceptionLog exceptionLog) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(ExceptionLogService.class).saveExceptionLog(exceptionLog);
            }
        };
    }

}