package com.ican.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 用户信息
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickname;
}
