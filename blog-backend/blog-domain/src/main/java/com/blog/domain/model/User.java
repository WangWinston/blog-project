package com.blog.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User Entity
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * User ID
     */
    private Long id;

    /**
     * Username
     */
    private String username;

    /**
     * Password (encrypted)
     */
    private String password;

    /**
     * Email
     */
    private String email;

    /**
     * Avatar URL
     */
    private String avatar;

    /**
     * User role
     */
    private String role;

    /**
     * GitHub ID (for OAuth)
     */
    private String githubId;

    /**
     * Profile info
     */
    private Profile profile;

    /**
     * Status (0: disabled, 1: enabled)
     */
    private Integer status;

    /**
     * Created time
     */
    private LocalDateTime createdTime;

    /**
     * Updated time
     */
    private LocalDateTime updatedTime;

    /**
     * Check if user is admin
     */
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    /**
     * Check if user is enabled
     */
    public boolean isEnabled() {
        return status != null && status == 1;
    }
}