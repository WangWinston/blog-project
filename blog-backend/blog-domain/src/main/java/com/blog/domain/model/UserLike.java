package com.blog.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User Like Record
 */
@Data
public class UserLike implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Like ID
     */
    private Long id;

    /**
     * User ID
     */
    private Long userId;

    /**
     * Article ID
     */
    private Long articleId;

    /**
     * Created time
     */
    private LocalDateTime createdTime;
}