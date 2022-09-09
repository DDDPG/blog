package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类选项VO
 *
 * @author ican
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryOptionVO {

    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String categoryName;
}
