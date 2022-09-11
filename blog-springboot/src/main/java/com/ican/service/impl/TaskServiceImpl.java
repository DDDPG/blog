package com.ican.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ican.entity.Task;
import com.ican.exception.TaskException;
import com.ican.mapper.TaskMapper;
import com.ican.service.TaskService;
import com.ican.utils.ScheduleUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<Task> taskList = taskMapper.selectList(null);
        for (Task task : taskList) {
            ScheduleUtils.createScheduleJob(scheduler, task);
        }
    }

}
