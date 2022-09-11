package com.ican.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ican.entity.Task;
import org.springframework.stereotype.Repository;

/**
 * 定时任务Mapper
 *
 * @author ican
 */
@Repository
public interface TaskMapper extends BaseMapper<Task> {
}
