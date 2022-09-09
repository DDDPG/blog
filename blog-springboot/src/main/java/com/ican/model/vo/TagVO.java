package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签VO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagVO {

    /**
     * 标签id
     */
    private Integer id;

    /**
     * 标签名
     */
    private String tagName;
}
