package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 访问量VO
 *
 * @author ican
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniqueViewVO {

    /**
     * 日期
     */
    private String date;

    /**
     * 访问量
     */
    private Integer viewCount;
}
