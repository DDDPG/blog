package com.ican.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 登录访问信息
 *
 * @author ican
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Visitor {
    /**
     * 登录id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * ip来源
     */
    private String ipSource;
    /**
     * 操作系统
     */
    private String os;

    /**
     * 访问浏览器
     */
    private String browser;
    /**
     * 注册时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 上次登录时间
     */
    private LocalDateTime loginTime;

}
