package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 图片信息DTO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PictureInfoDTO {

    /**
     * 图片id
     */
    @NotNull(message = "照片id不能为空")
    private Integer id;

    /**
     * 图片名称
     */
    @NotBlank(message = "照片名称不能为空")
    private String pictureName;

    /**
     * 图片描述
     */
    private String pictureDesc;
}
