package com.blog.common.enums;

import lombok.Getter;

/**
 * Article Status Enum
 */
@Getter
public enum ArticleStatus {

    DRAFT("DRAFT", "Draft"),
    PUBLISHED("PUBLISHED", "Published"),
    UNPUBLISHED("UNPUBLISHED", "Unpublished"),
    SCHEDULED("SCHEDULED", "Scheduled");

    private final String code;
    private final String description;

    ArticleStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ArticleStatus fromCode(String code) {
        for (ArticleStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return DRAFT;
    }
}