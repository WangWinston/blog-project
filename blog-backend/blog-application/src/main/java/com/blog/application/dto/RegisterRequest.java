package com.blog.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Register Request DTO
 */
@Data
public class RegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String email;
    private String captchaToken;
}