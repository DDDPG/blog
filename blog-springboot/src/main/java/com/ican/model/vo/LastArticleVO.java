package com.ican.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 最新文章VO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LastArticleVO {

    /**
     * 文章id
     */
    private Integer id;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 发表时间
     */
    private LocalDateTime createTime;
}
