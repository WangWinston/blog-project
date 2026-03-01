package com.blog.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Dashboard Statistics DTO
 */
@Data
public class DashboardStatsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long totalArticles;
    private Long publishedArticles;
    private Long draftArticles;
    private Long totalUsers;
    private Long totalComments;
    private Long pendingComments;
    private Long totalViews;
    private Long totalLikes;
    private Long todayViews;
    private Long todayComments;
}