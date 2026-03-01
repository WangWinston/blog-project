package com.blog.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Category DTO
 */
@Data
public class CategoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String slug;
    private String description;
    private Long parentId;
    private Integer sortOrder;
    private String icon;
    private Integer articleCount;
    private java.util.List<CategoryDTO> children;
}