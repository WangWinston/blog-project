package com.blog.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Tag Entity
 */
@Data
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Tag ID
     */
    private Long id;

    /**
     * Tag name
     */
    private String name;

    /**
     * Slug (URL-friendly identifier)
     */
    private String slug;

    /**
     * Description
     */
    private String description;

    /**
     * Tag color (hex color code)
     */
    private String color;

    /**
     * Article count
     */
    private Integer articleCount;

    /**
     * Created time
     */
    private LocalDateTime createdTime;

    /**
     * Updated time
     */
    private LocalDateTime updatedTime;
}