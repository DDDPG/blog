package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文章归档
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveVO {

    /**
     * 文章id
     */
    private Integer id;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 发表时间
     */
    private LocalDateTime createTime;
}
