package com.ican.controller;

import com.ican.annotation.OptLog;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.dto.StatusDTO;
import com.ican.model.dto.TaskDTO;
import com.ican.model.dto.TaskRunDTO;
import com.ican.model.vo.PageResult;
import com.ican.model.vo.Result;
import com.ican.model.vo.TaskBackVO;
import com.ican.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ican.constant.OptTypeConstant.*;

/**
 * 定时任务控制器
 *
 * @author ican
 */
@Api(tags = "定时任务模块")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 查看定时任务列表
     *
     * @param condition 条件
     * @return {@link Result<TaskBackVO>} 定时任务列表
     */
    @ApiOperation("查看定时任务列表")
    @GetMapping("/admin/tasks")
    public Result<PageResult<TaskBackVO>> listTasks(ConditionDTO condition) {
        return Result.success(taskService.listTasks(condition));
    }

    /**
     * 新增定时任务
     *
     * @param task 定时任务信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE)
    @ApiOperation("新增定时任务")
    @PostMapping("/admin/tasks")
    public Result<?> saveTask(@RequestBody TaskDTO task) {
        taskService.saveTask(task);
        return Result.success();
    }

    /**
     * 修改定时任务
     *
     * @param task 定时任务信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation("修改定时任务")
    @PutMapping("/admin/tasks")
    public Result<?> updateTask(@RequestBody TaskDTO task) {
        taskService.updateTask(task);
        return Result.success();
    }

    /**
     * 删除定时任务
     *
     * @param taskIdList 定时任务id集合
     * @return {@link Result<>}
     */
    @OptLog(optType = DELETE)
    @ApiOperation("删除定时任务")
    @DeleteMapping("/admin/tasks")
    public Result<?> deleteTask(@RequestBody List<Integer> taskIdList) {
        taskService.deleteTask(taskIdList);
        return Result.success();
    }

    /**
     * 修改定时任务状态
     *
     * @param taskStatus 定时任务状态
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation("修改定时任务状态")
    @PutMapping("/admin/tasks/changeStatus")
    public Result<?> updateTaskStatus(@RequestBody StatusDTO taskStatus) {
        taskService.updateTaskStatus(taskStatus);
        return Result.success();
    }

    /**
     * 定时任务立即执行一次
     *
     * @param taskRun 运行定时任务
     * @return {@link Result<>}
     */
    @ApiOperation("定时任务立即执行一次")
    @PostMapping("/admin/tasks/run")
    public Result<?> runTask(@RequestBody TaskRunDTO taskRun) {
        taskService.runTask(taskRun);
        return Result.success();
    }

}
