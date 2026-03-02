package com.blog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * Login Request DTO
 */
@Data
@Schema(description = "Login request")
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Username cannot be blank")
    @Schema(description = "Username", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Schema(description = "Password", example = "admin123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "Captcha token cannot be blank")
    @Schema(description = "Captcha token", requiredMode = Schema.RequiredMode.REQUIRED)
    private String captchaToken;
}