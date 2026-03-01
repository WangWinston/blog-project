package com.blog.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Article Version DTO
 */
@Data
public class ArticleVersionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long articleId;
    private Integer version;
    private String title;
    private String content;
    private String changeLog;
    private Long editorId;
    private String editorName;
    private LocalDateTime createdTime;
}