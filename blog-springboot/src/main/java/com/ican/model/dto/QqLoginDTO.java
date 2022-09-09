package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * QQ登录DTO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QqLoginDTO {

    /**
     * openId
     */
    @NotBlank(message = "openId不能为空")
    private String openId;

    /**
     * accessToken
     */
    @NotBlank(message = "accessToken不能为空")
    private String accessToken;
}
