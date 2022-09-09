package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微博token
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeiboTokenVO {

    /**
     * 微博uid
     */
    private String uid;

    /**
     * 访问令牌
     */
    private String access_token;
}
