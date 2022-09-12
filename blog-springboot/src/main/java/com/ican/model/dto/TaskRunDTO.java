package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 定时任务运行
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRunDTO {

    /**
     * 任务id
     */
    private Integer id;

    /**
     * 任务组别
     */
    private String taskGroup;
}
