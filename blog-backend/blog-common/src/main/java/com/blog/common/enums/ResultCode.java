package com.blog.common.enums;

import lombok.Getter;

/**
 * Result Code Enum
 */
@Getter
public enum ResultCode {

    // Success
    SUCCESS(200, "Success"),

    // Client Errors (4xx)
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    VALIDATION_ERROR(422, "Validation Error"),

    // Server Errors (5xx)
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    // Business Errors (1xxx)
    USER_NOT_FOUND(1001, "User not found"),
    USER_ALREADY_EXISTS(1002, "User already exists"),
    INVALID_PASSWORD(1003, "Invalid password"),
    ACCOUNT_DISABLED(1004, "Account is disabled"),
    CAPTCHA_ERROR(1005, "Captcha verification failed"),

    // Article Errors (2xxx)
    ARTICLE_NOT_FOUND(2001, "Article not found"),
    ARTICLE_ALREADY_PUBLISHED(2002, "Article already published"),
    ARTICLE_ALREADY_DRAFT(2003, "Article is already a draft"),
    ARTICLE_VERSION_NOT_FOUND(2004, "Article version not found"),

    // Category Errors (3xxx)
    CATEGORY_NOT_FOUND(3001, "Category not found"),
    CATEGORY_ALREADY_EXISTS(3002, "Category already exists"),
    CATEGORY_HAS_CHILDREN(3003, "Category has children, cannot delete"),

    // Tag Errors (4xxx)
    TAG_NOT_FOUND(4001, "Tag not found"),
    TAG_ALREADY_EXISTS(4002, "Tag already exists"),

    // Comment Errors (5xxx)
    COMMENT_NOT_FOUND(5001, "Comment not found"),
    COMMENT_ALREADY_DELETED(5002, "Comment already deleted"),

    // File Errors (6xxx)
    FILE_UPLOAD_ERROR(6001, "File upload failed"),
    FILE_NOT_FOUND(6002, "File not found");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}