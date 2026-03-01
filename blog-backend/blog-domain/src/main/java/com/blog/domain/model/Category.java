package com.blog.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Category Entity (Tree Structure)
 */
@Data
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Category ID
     */
    private Long id;

    /**
     * Category name
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
     * Parent category ID (null for root)
     */
    private Long parentId;

    /**
     * Sort order
     */
    private Integer sortOrder;

    /**
     * Icon
     */
    private String icon;

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

    /**
     * Children categories
     */
    private List<Category> children = new ArrayList<>();

    /**
     * Check if this is a root category
     */
    public boolean isRoot() {
        return parentId == null || parentId == 0;
    }

    /**
     * Add child category
     */
    public void addChild(Category child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }
}