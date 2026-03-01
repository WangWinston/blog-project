package com.blog.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Article Version Entity
 */
@Data
public class ArticleVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Version ID
     */
    private Long id;

    /**
     * Article ID
     */
    private Long articleId;

    /**
     * Version number
     */
    private Integer version;

    /**
     * Title at this version
     */
    private String title;

    /**
     * Content at this version
     */
    private String content;

    /**
     * Change log/commit message
     */
    private String changeLog;

    /**
     * Editor ID
     */
    private Long editorId;

    /**
     * Editor name
     */
    private String editorName;

    /**
     * Created time
     */
    private LocalDateTime createdTime;
}