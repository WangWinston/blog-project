package com.blog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Article DTO
 */
@Data
@Schema(description = "Article data")
public class ArticleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Article ID", example = "1")
    private Long id;

    @NotBlank(message = "Title cannot be blank", groups = {Create.class, Update.class})
    @Size(max = 200, message = "Title must not exceed 200 characters")
    @Schema(description = "Article title", example = "My First Blog Post")
    private String title;

    @Schema(description = "URL-friendly slug", example = "my-first-blog-post")
    private String slug;

    @Size(max = 500, message = "Summary must not exceed 500 characters")
    @Schema(description = "Article summary/excerpt", example = "This is a summary of my blog post...")
    private String summary;

    @Schema(description = "Article content in Markdown format", example = "# Hello World\n\nThis is my content...")
    private String content;

    @Schema(description = "Cover image URL", example = "/uploads/image/2026/03/03/cover.jpg")
    private String coverImage;

    @Schema(description = "Category ID", example = "1")
    private Long categoryId;

    @Schema(description = "Category name", example = "Technology")
    private String categoryName;

    @Schema(description = "Tag IDs", example = "[1, 2, 3]")
    private List<Long> tagIds;

    @Schema(description = "Tag list")
    private List<TagDTO> tags;

    @Schema(description = "Author ID", example = "1")
    private Long authorId;

    @Schema(description = "Author name", example = "admin")
    private String authorName;

    @Schema(description = "Article status", example = "PUBLISHED", allowableValues = {"DRAFT", "PUBLISHED", "UNPUBLISHED", "SCHEDULED"})
    private String status;

    @Schema(description = "Is top/pinned", example = "false")
    private Boolean isTop;

    @Schema(description = "Is recommended", example = "false")
    private Boolean isRecommend;

    @Schema(description = "View count", example = "100")
    private Integer viewCount;

    @Schema(description = "Like count", example = "10")
    private Integer likeCount;

    @Schema(description = "Comment count", example = "5")
    private Integer commentCount;

    @Schema(description = "Scheduled publish time", example = "2026-03-04T10:00:00")
    private LocalDateTime scheduledPublishTime;

    @Schema(description = "Published time", example = "2026-03-03T10:00:00")
    private LocalDateTime publishedTime;

    @Schema(description = "Created time")
    private LocalDateTime createdTime;

    @Schema(description = "Updated time")
    private LocalDateTime updatedTime;

    @Schema(description = "Estimated read time in minutes", example = "5")
    private Integer readTime;

    @Schema(description = "Whether current user liked this article")
    private Boolean isLiked;

    @Schema(description = "Whether current user favorited this article")
    private Boolean isFavorited;

    /** Validation group for create operation */
    public interface Create {}

    /** Validation group for update operation */
    public interface Update {}
}