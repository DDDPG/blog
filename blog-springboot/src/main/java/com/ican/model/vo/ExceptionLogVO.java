package com.ican.model.vo;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 异常日志VO
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionLogVO {

    /**
     * id
     */
    private Integer id;

    /**
     * 异常模块
     */
    private String module;

    /**
     * 异常uri
     */
    private String uri;

    /**
     * 异常名称
     */
    private String name;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 异常方法
     */
    private String errorMethod;

    /**
     * 异常信息
     */
    private String message;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 操作ip
     */
    private String ipAddress;

    /**
     * 操作地址
     */
    private String ipSource;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作时间
     */
    private LocalDateTime createTime;

}
