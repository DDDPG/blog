package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户密码
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDTO {

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /**
     * 新密码
     */
    @Size(min = 6, message = "密码不能少于6位")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
