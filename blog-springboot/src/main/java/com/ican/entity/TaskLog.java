package com.ican.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 定时任务日志
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskLog {

    /**
     * 任务日志id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 任务id
     */
    private Integer taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务组名
     */
    private String taskGroup;

    /**
     * 调用目标
     */
    private String invokeTarget;

    /**
     * 日志信息
     */
    private String taskMessage;

    /**
     * 任务状态 (0暂停 1运行)
     */
    private Integer status;

    /**
     * 错误信息
     */
    private String errorInfo;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 停止时间
     */
    private Date endTime;

}
