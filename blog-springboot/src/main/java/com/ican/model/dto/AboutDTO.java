package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关于我信息
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AboutDTO {

    /**
     * 关于我
     */
    private String aboutContent;

}
