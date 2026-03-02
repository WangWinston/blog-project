package com.blog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User DTO
 */
@Data
@Schema(description = "User data")
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "Username", example = "admin")
    private String username;

    @Schema(description = "Email", example = "admin@blog.com")
    private String email;

    @Schema(description = "Avatar URL", example = "/uploads/avatar.jpg")
    private String avatar;

    @Schema(description = "User role", example = "ADMIN", allowableValues = {"ADMIN", "USER"})
    private String role;

    @Schema(description = "User status", example = "ACTIVE", allowableValues = {"ACTIVE", "DISABLED"})
    private Integer status;

    @Schema(description = "GitHub ID")
    private String githubId;

    @Schema(description = "Nickname", example = "Blog Admin")
    private String nickname;

    @Schema(description = "Bio/Introduction", example = "A software developer")
    private String bio;

    @Schema(description = "Personal website", example = "https://example.com")
    private String website;

    @Schema(description = "Location", example = "Beijing, China")
    private String location;

    @Schema(description = "Created time")
    private LocalDateTime createdAt;
}