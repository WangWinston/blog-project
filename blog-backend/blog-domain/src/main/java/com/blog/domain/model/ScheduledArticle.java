package com.blog.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Scheduled Article Entity
 */
@Data
public class ScheduledArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Scheduled article ID
     */
    private Long id;

    /**
     * Article ID
     */
    private Long articleId;

    /**
     * Article title (for display)
     */
    private String articleTitle;

    /**
     * Scheduled publish time
     */
    private LocalDateTime scheduledTime;

    /**
     * Status (PENDING, PUBLISHED, FAILED)
     */
    private String status;

    /**
     * Error message (if failed)
     */
    private String errorMessage;

    /**
     * Created time
     */
    private LocalDateTime createdTime;

    /**
     * Updated time
     */
    private LocalDateTime updatedTime;

    /**
     * Check if the scheduled time has arrived
     */
    public boolean shouldPublish() {
        return "PENDING".equals(status) && scheduledTime != null &&
                LocalDateTime.now().isAfter(scheduledTime) || LocalDateTime.now().isEqual(scheduledTime);
    }
}