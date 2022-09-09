package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Gitee Token
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO {

    /**
     * 访问令牌
     */
    private String access_token;
}
