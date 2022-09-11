package com.ican.quartz;

import com.ican.entity.Task;
import com.ican.utils.TaskInvokeUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（禁止并发执行）
 * 
 * @author ican
 *
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, Task task) throws Exception
    {
        TaskInvokeUtils.invokeMethod(task);
    }
}
