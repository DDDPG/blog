package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 网站信息配置DTO
 *
 * @author ican
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebsiteDTO {

    /**
     * 作者头像
     */
    private String authorAvatar;

    /**
     * 网站名称
     */
    private String websiteName;

    /**
     * 网站作者
     */
    private String websiteAuthor;

    /**
     * 网站介绍
     */
    private String websiteIntro;

    /**
     * 网站创建时间
     */
    private String websiteCreateTime;

    /**
     * 网站备案号
     */
    private String websiteRecordNumber;

    /**
     * 社交登录列表
     */
    private List<String> socialLoginList;

    /**
     * 社交url列表
     */
    private List<String> socialUrlList;

    /**
     * github
     */
    private String github;

    /**
     * gitee
     */
    private String gitee;

    /**
     * qq
     */
    private String qq;

    /**
     * 哔哩哔哩
     */
    private String bilibili;

    /**
     * 游客头像
     */
    private String touristAvatar;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 是否评论审核
     */
    private Integer isCommentCheck;

    /**
     * 是否留言审核
     */
    private Integer isMessageCheck;

    /**
     * 是否邮箱通知
     */
    private Integer isEmailNotice;

    /**
     * 是否开启音乐播放器
     */
    private Integer isMusicPlayer;

    /**
     * 是否打赏
     */
    private Integer isReward;

    /**
     * 微信二维码
     */
    private String weiXinQRCode;

    /**
     * 支付宝二维码
     */
    private String alipayQRCode;

    /**
     * 网易云歌单id
     */
    private String musicId;

}
