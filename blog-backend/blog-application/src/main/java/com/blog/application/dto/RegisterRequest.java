package com.blog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * Register Request DTO
 */
@Data
@Schema(description = "Register request")
public class RegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Username", example = "newuser", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @Schema(description = "Password", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Email(message = "Invalid email format")
    @Schema(description = "Email", example = "user@example.com")
    private String email;

    @NotBlank(message = "Captcha token cannot be blank")
    @Schema(description = "Captcha token", requiredMode = Schema.RequiredMode.REQUIRED)
    private String captchaToken;
}