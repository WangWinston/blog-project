package com.blog.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Tag DTO
 */
@Data
public class TagDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String slug;
    private String description;
    private String color;
    private Integer articleCount;
}