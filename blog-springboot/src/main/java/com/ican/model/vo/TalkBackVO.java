package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 说说后台VO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TalkBackVO {

    /**
     * 说说id
     */
    private Integer id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 说说内容
     */
    private String talkContent;

    /**
     * 图片
     */
    private String images;

    /**
     * 图片列表
     */
    private List<String> imgList;

    /**
     * 是否置顶 (0否 1是)
     */
    private Integer isTop;

    /**
     * 说说状态 (1公开 2私密)
     */
    private Integer status;

    /**
     * 是否定时发布 (0否 1是)
     */
    private Integer isTime;

    /**
     * 发布时间
     */
    private LocalDateTime createTime;

}
