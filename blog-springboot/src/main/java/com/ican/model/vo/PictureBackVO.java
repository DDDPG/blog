package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 图片VO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PictureBackVO {

    /**
     * id
     */
    private Integer id;

    /**
     * 图片路径
     */
    private String pictureUrl;

    /**
     * 图片名称
     */
    private String pictureName;

    /**
     * 图片描述
     */
    private String pictureDesc;

}
