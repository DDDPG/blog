package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QQ用户信息
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QqUserInfoVO {

    /**
     * QQ头像
     */
    private String figureurl_qq_1;

    /**
     * 昵称
     */
    private String nickname;

}
