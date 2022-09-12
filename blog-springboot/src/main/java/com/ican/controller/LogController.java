package com.ican.controller;

import com.ican.model.vo.*;
import com.ican.model.dto.ConditionDTO;
import com.ican.service.ExceptionLogService;
import com.ican.service.OperationLogService;
import com.ican.service.TaskLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 日志控制器
 *
 * @author ican
 */
@Api(tags = "日志模块")
@RestController
public class LogController {

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private ExceptionLogService exceptionLogService;

    @Autowired
    private TaskLogService taskLogService;

    /**
     * 查看操作日志
     *
     * @param condition 条件
     * @return {@link Result<OperationLogVO>} 操作日志列表
     */
    @ApiOperation(value = "查看操作日志")
    @GetMapping("/admin/operation/logs")
    public Result<PageResult<OperationLogVO>> listOperationLogVO(ConditionDTO condition) {
        return Result.success(operationLogService.listOperationLogVO(condition));
    }

    /**
     * 删除操作日志
     *
     * @param logIdList 日志id集合
     * @return {@link Result<>}
     */
    @ApiOperation(value = "删除操作日志")
    @DeleteMapping("/admin/operation/logs")
    public Result<?> deleteOperationLog(@RequestBody List<Integer> logIdList) {
        operationLogService.removeByIds(logIdList);
        return Result.success();
    }

    /**
     * 查看异常日志
     *
     * @param condition 条件
     * @return {@link Result<OperationLogVO>} 异常日志列表
     */
    @ApiOperation(value = "查看异常日志")
    @GetMapping("/admin/exception/logs")
    public Result<PageResult<ExceptionLogVO>> listExceptionLogVO(ConditionDTO condition) {
        return Result.success(exceptionLogService.listExceptionLogVO(condition));
    }

    /**
     * 删除异常日志
     *
     * @param logIdList 日志id集合
     * @return {@link Result<>}
     */
    @ApiOperation(value = "删除异常日志")
    @DeleteMapping("/admin/exception/logs")
    public Result<?> deleteExceptionLog(@RequestBody List<Integer> logIdList) {
        exceptionLogService.removeByIds(logIdList);
        return Result.success();
    }

    /**
     * 导出操作日志
     *
     * @return {@link Result<>}
     */
    @ApiOperation(value = "导出操作日志")
    @GetMapping("/admin/operation/logs/export")
    public Result<?> exportExceptionLog() {
        operationLogService.exportExceptionLog();
        return Result.success();
    }

    /**
     * 查看定时任务日志
     *
     * @param condition 条件
     * @return {@link PageResult<TaskLogVO>} 后台定时任务日志
     */
    @ApiOperation("查看定时任务日志")
    @GetMapping("/admin/task/logs")
    public Result<PageResult<TaskLogVO>> listTaskLogs(ConditionDTO condition) {
        return Result.success(taskLogService.listTaskLogs(condition));
    }

    /**
     * 删除定时任务日志
     *
     * @param logIdList 日志id集合
     * @return {@link Result<>}
     */
    @ApiOperation("删除定时任务的日志")
    @DeleteMapping("/admin/task/logs")
    public Result<?> deleteTaskLog(@RequestBody List<Integer> logIdList) {
        taskLogService.removeByIds(logIdList);
        return Result.success();
    }

    /**
     * 清空定时任务日志
     *
     * @return {@link Result<>}
     */
    @ApiOperation("删除定时任务的日志")
    @DeleteMapping("/admin/task/logs/clean")
    public Result<?> cleanTaskLog() {
        taskLogService.cleanTaskLog();
        return Result.success();
    }
}
