package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 标签DTO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    /**
     * 标签id
     */
    private Integer id;

    /**
     * 标签名
     */
    @NotBlank(message = "标签名不能为空")
    private String tagName;
}
