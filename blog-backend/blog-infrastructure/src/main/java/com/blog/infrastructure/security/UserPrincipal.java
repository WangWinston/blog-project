package com.blog.infrastructure.security;

import lombok.Getter;
import java.io.Serializable;

/**
 * User Principal for Security Context
 */
@Getter
public class UserPrincipal implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long userId;
    private final String username;
    private final String role;

    public UserPrincipal(Long userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}