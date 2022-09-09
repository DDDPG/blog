package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 用户状态
 *
 * @author ican
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusDTO {
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
