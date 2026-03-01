package com.blog.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Login Response DTO
 */
@Data
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserDTO user;

    public static LoginResponse of(String accessToken, String refreshToken, Long expiresIn, UserDTO user) {
        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(expiresIn);
        response.setUser(user);
        return response;
    }
}