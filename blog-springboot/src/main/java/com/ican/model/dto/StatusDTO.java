package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 状态DTO
 *
 * @author ican
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDTO {
    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    private Integer id;

    /**
     * 禁用状态
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
}
