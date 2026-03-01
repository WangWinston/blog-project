package com.blog.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Article DTO
 */
@Data
public class ArticleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String coverImage;
    private Long categoryId;
    private String categoryName;
    private List<Long> tagIds;
    private List<TagDTO> tags;
    private Long authorId;
    private String authorName;
    private String status;
    private Boolean isTop;
    private Boolean isRecommend;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime scheduledPublishTime;
    private LocalDateTime publishedTime;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private Integer readTime;

    // For interaction status
    private Boolean isLiked;
    private Boolean isFavorited;
}