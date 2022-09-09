package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QQ token信息
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QqTokenVO {

    /**
     * openid
     */
    private String openid;

    /**
     * 客户端id
     */
    private String client_id;
}
