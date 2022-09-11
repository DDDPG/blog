package com.ican.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态枚举
 *
 * @author ican
 */
@Getter
@AllArgsConstructor
public enum JobStatusEnum {

    /**
     * 运行
     */
    RUNNING(1),

    /**
     * 暂停
     */
    PAUSE(0);

    /**
     * 状态
     */
    private final Integer status;
}
