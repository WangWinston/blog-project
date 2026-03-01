package com.blog.common.enums;

import lombok.Getter;

/**
 * User Role Enum
 */
@Getter
public enum UserRole {

    ADMIN("ADMIN", "Administrator"),
    USER("USER", "Normal User");

    private final String code;
    private final String description;

    UserRole(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static UserRole fromCode(String code) {
        for (UserRole role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        return USER;
    }
}