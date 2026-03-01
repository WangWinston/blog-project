package com.blog.common.enums;

import lombok.Getter;

/**
 * Comment Status Enum
 */
@Getter
public enum CommentStatus {

    PENDING("PENDING", "Pending Review"),
    APPROVED("APPROVED", "Approved"),
    REJECTED("REJECTED", "Rejected"),
    DELETED("DELETED", "Deleted");

    private final String code;
    private final String description;

    CommentStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static CommentStatus fromCode(String code) {
        for (CommentStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return PENDING;
    }
}