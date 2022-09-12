package com.ican.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ican.constant.ScheduleConstants;
import com.ican.entity.Task;
import com.ican.enums.TaskStatusEnum;
import com.ican.exception.ServiceException;
import com.ican.mapper.TaskMapper;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.dto.StatusDTO;
import com.ican.model.dto.TaskDTO;
import com.ican.model.dto.TaskRunDTO;
import com.ican.model.vo.PageResult;
import com.ican.model.vo.TaskBackVO;
import com.ican.service.TaskService;
import com.ican.utils.BeanCopyUtils;
import com.ican.utils.CronUtils;
import com.ican.utils.PageUtils;
import com.ican.utils.ScheduleUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author ican
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private TaskMapper taskMapper;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理
     * 注：不能手动修改数据库ID和任务组名，否则会导致脏数据
     */
    @PostConstruct
    public void init() throws SchedulerException {
        scheduler.clear();
        List<Task> taskList = taskMapper.selectList(null);
        for (Task task : taskList) {
            // 创建定时任务
            ScheduleUtils.createScheduleJob(scheduler, task);
        }
    }

    @Override
    public PageResult<TaskBackVO> listTasks(ConditionDTO condition) {
        // 查询定时任务数量
        Integer count = taskMapper.countTaskBackVO(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询分类列表
        List<TaskBackVO> taskBackVOList = taskMapper.selectTaskBackVO(PageUtils.getLimit(), PageUtils.getSize(), condition);
        return new PageResult<>(taskBackVOList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveTask(TaskDTO task) {
        if (CronUtils.isValid(task.getCronExpression())) {
            throw new ServiceException("Cron表达式无效!");
        }
        Task newTask = BeanCopyUtils.copyBean(task, Task.class);
        // 新增定时任务
        int row = taskMapper.insert(newTask);
        // 创建定时任务
        if (row > 0) {
            ScheduleUtils.createScheduleJob(scheduler, newTask);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTask(TaskDTO task) {
        if (CronUtils.isValid(task.getCronExpression())) {
            throw new ServiceException("Cron表达式无效!");
        }
        Task existTask = taskMapper.selectById(task.getId());
        Task newTask = BeanCopyUtils.copyBean(task, Task.class);
        // 更新定时任务
        int row = taskMapper.updateById(newTask);
        if (row > 0) {
            try {
                updateSchedulerJob(newTask, existTask.getTaskGroup());
            } catch (SchedulerException e) {
                throw new ServiceException("更新定时任务异常");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTask(List<Integer> taskIdList) {
        List<Task> taskList = taskMapper.selectList(new LambdaQueryWrapper<Task>().in(Task::getId, taskIdList));
        // 删除定时任务
        int row = taskMapper.delete(new LambdaQueryWrapper<Task>().in(Task::getId, taskIdList));
        if (row > 0) {
            taskList.forEach(item -> {
                try {
                    scheduler.deleteJob(ScheduleUtils.getJobKey(item.getId(), item.getTaskGroup()));
                } catch (SchedulerException e) {
                    throw new ServiceException("删除定时任务异常");
                }
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTaskStatus(StatusDTO taskStatus) {
        Task task = taskMapper.selectById(taskStatus.getId());
        // 相同不用更新
        if (task.getStatus().equals(taskStatus.getStatus())) {
            return;
        }
        // 更新数据库中的定时任务状态
        Task newTask = Task.builder()
                .id(taskStatus.getId())
                .status(taskStatus.getStatus())
                .build();
        int row = taskMapper.updateById(newTask);
        // 获取定时任务状态、id、任务组
        Integer status = taskStatus.getStatus();
        Integer taskId = task.getId();
        String taskGroup = task.getTaskGroup();
        if (row > 0) {
            // 更新定时任务
            try {
                if (TaskStatusEnum.RUNNING.getStatus().equals(status)) {
                    scheduler.resumeJob(ScheduleUtils.getJobKey(taskId, taskGroup));
                }
                if (TaskStatusEnum.PAUSE.getStatus().equals(status)) {
                    scheduler.pauseJob(ScheduleUtils.getJobKey(taskId, taskGroup));
                }
            } catch (SchedulerException e) {
                throw new ServiceException("更新定时任务状态异常");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void runTask(TaskRunDTO taskRun) {
        Integer taskId = taskRun.getId();
        String taskGroup = taskRun.getTaskGroup();
        // 查询定时任务
        Task task = taskMapper.selectById(taskRun.getId());
        // 设置参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, task);
        try {
            scheduler.triggerJob(ScheduleUtils.getJobKey(taskId, taskGroup), dataMap);
        } catch (SchedulerException e) {
            throw new ServiceException("执行定时任务异常");
        }
    }

    /**
     * 更新任务
     *
     * @param job      任务对象
     * @param jobGroup 任务组名
     */
    public void updateSchedulerJob(Task job, String jobGroup) throws SchedulerException {
        Integer taskId = job.getId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(taskId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }
}
