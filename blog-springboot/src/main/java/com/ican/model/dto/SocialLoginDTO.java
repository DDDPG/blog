package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Gitee和Github登录DTO
 *
 * @author ican
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginDTO {

    /**
     * code
     */
    @NotBlank(message = "code不能为空")
    private String code;

    /**
     * state
     */
    @NotBlank(message = "state不能为空")
    private String state;

}
