package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户区域VO
 *
 * @author ican
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserZoneVO {

    /**
     * 地区名
     */
    private String name;

    /**
     * 数量
     */
    private Long value;

}
