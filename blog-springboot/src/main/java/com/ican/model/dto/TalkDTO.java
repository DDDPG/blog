package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 说说DTO
 *
 * @author ican
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TalkDTO {

    /**
     * 说说id
     */
    private Integer id;

    /**
     * 说说内容
     */
    @NotBlank(message = "说说内容不能为空")
    private String talkContent;

    /**
     * 说说图片
     */
    private String images;

    /**
     * 是否置顶 (0否 1是)
     */
    @NotNull(message = "置顶状态不能为空")
    private Integer isTop;

    /**
     * 说说状态 (1公开 2私密)
     */
    @NotNull(message = "说说状态不能为空")
    private Integer status;

    /**
     * 是否定时发布 (0否 1是)
     */
    private Integer isTime;

    /**
     * 定时发布时间
     */
    private Date createTime;

}
