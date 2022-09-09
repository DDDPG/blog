package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 轮播图VO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PictureVO {

    /**
     * 图片id
     */
    private Integer id;

    /**
     * 图片链接
     */
    private String pictureUrl;
}
