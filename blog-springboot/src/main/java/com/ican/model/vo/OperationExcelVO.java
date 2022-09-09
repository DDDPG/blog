package com.ican.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 操作日志Excel
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OperationExcelVO {

    /**
     * 操作模块
     */
    @ExcelProperty("操作模块")
    private String module;

    /**
     * 操作uri
     */
    @ExcelProperty("操作uri")
    private String uri;

    /**
     * 操作类型
     */
    @ExcelProperty("操作类型")
    private String type;

    /**
     * 操作方法
     */
    @ExcelProperty("操作方法")
    private String name;

    /**
     * 操作描述
     */
    @ExcelProperty("操作描述")
    private String description;

    /**
     * 请求方式
     */
    @ExcelProperty("请求方式")
    private String method;

    /**
     * 请求参数
     */
    @ExcelProperty("请求参数")
    private String params;

    /**
     * 返回数据
     */
    @ExcelProperty("返回数据")
    private String data;

    /**
     * 用户昵称
     */
    @ExcelProperty("用户昵称")
    private String nickname;

    /**
     * 操作ip
     */
    @ExcelProperty("操作ip")
    private String ipAddress;

    /**
     * 操作地址
     */
    @ExcelProperty("操作地址")
    private String ipSource;

    /**
     * 操作耗时 (毫秒)
     */
    @ExcelProperty("操作耗时 (毫秒)")
    private Long times;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
