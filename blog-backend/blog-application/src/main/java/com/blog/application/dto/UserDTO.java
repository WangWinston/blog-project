package com.blog.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * User DTO
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;
    private String avatar;
    private String role;
    private String nickname;
    private String bio;
    private String website;
    private String location;
}