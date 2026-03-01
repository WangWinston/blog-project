package com.blog.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Comment Entity (supports nested replies)
 */
@Data
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Comment ID
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
     * User ID
     */
    private Long userId;

    /**
     * User name
     */
    private String userName;

    /**
     * User avatar
     */
    private String userAvatar;

    /**
     * Parent comment ID (null for root comment)
     */
    private Long parentId;

    /**
     * Root comment ID (for nested replies, points to the top-level comment)
     */
    private Long rootId;

    /**
     * Reply to user ID
     */
    private Long replyToUserId;

    /**
     * Reply to user name
     */
    private String replyToUserName;

    /**
     * Comment content
     */
    private String content;

    /**
     * Status (PENDING, APPROVED, SPAM, DELETED)
     */
    private String status;

    /**
     * Like count
     */
    private Integer likeCount;

    /**
     * IP address
     */
    private String ipAddress;

    /**
     * User agent
     */
    private String userAgent;

    /**
     * Created time
     */
    private LocalDateTime createdTime;

    /**
     * Updated time
     */
    private LocalDateTime updatedTime;

    /**
     * Child comments (replies)
     */
    private List<Comment> children = new ArrayList<>();

    /**
     * Check if this is a root comment
     */
    public boolean isRoot() {
        return parentId == null || parentId == 0;
    }

    /**
     * Check if comment is approved
     */
    public boolean isApproved() {
        return "APPROVED".equals(status);
    }

    /**
     * Add child comment
     */
    public void addChild(Comment child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }
}