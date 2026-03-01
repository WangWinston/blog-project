package com.blog.domain.model;

import lombok.Data;

import java.io.Serializable;

/**
 * User Profile Value Object
 */
@Data
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Nickname
     */
    private String nickname;

    /**
     * Bio/Introduction
     */
    private String bio;

    /**
     * Personal website
     */
    private String website;

    /**
     * Location
     */
    private String location;

    /**
     * GitHub username
     */
    private String githubUsername;
}