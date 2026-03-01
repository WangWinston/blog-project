package com.blog.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Article Aggregate Root
 */
@Data
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Article ID
     */
    private Long id;

    /**
     * Title
     */
    private String title;

    /**
     * Slug (URL-friendly identifier)
     */
    private String slug;

    /**
     * Summary/Excerpt
     */
    private String summary;

    /**
     * Content (Markdown)
     */
    private Content content;

    /**
     * Cover image URL
     */
    private String coverImage;

    /**
     * Category ID
     */
    private Long categoryId;

    /**
     * Category name (for display)
     */
    private String categoryName;

    /**
     * Tag IDs
     */
    private List<Long> tagIds = new ArrayList<>();

    /**
     * Tag names (for display)
     */
    private List<String> tagNames = new ArrayList<>();

    /**
     * Author ID
     */
    private Long authorId;

    /**
     * Author name
     */
    private String authorName;

    /**
     * Status (DRAFT, PUBLISHED, UNPUBLISHED, SCHEDULED)
     */
    private String status;

    /**
     * Is top/pinned
     */
    private Boolean isTop;

    /**
     * Is recommended
     */
    private Boolean isRecommend;

    /**
     * View count
     */
    private Integer viewCount;

    /**
     * Like count
     */
    private Integer likeCount;

    /**
     * Comment count
     */
    private Integer commentCount;

    /**
     * Scheduled publish time
     */
    private LocalDateTime scheduledPublishTime;

    /**
     * Published time
     */
    private LocalDateTime publishedTime;

    /**
     * Created time
     */
    private LocalDateTime createdTime;

    /**
     * Updated time
     */
    private LocalDateTime updatedTime;

    /**
     * Check if article is published
     */
    public boolean isPublished() {
        return "PUBLISHED".equals(status);
    }

    /**
     * Check if article is draft
     */
    public boolean isDraft() {
        return "DRAFT".equals(status);
    }

    /**
     * Increment view count
     */
    public void incrementViewCount() {
        if (this.viewCount == null) {
            this.viewCount = 0;
        }
        this.viewCount++;
    }

    /**
     * Increment like count
     */
    public void incrementLikeCount() {
        if (this.likeCount == null) {
            this.likeCount = 0;
        }
        this.likeCount++;
    }

    /**
     * Decrement like count
     */
    public void decrementLikeCount() {
        if (this.likeCount != null && this.likeCount > 0) {
            this.likeCount--;
        }
    }
}