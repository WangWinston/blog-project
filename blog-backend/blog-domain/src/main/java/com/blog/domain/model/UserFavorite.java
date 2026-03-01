package com.blog.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User Favorite Record
 */
@Data
public class UserFavorite implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Favorite ID
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