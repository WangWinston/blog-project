package com.blog.interfaces.controller;

import com.blog.application.dto.ArticleDTO;
import com.blog.application.dto.CommentDTO;
import com.blog.application.dto.DashboardStatsDTO;
import com.blog.application.service.DashboardService;
import com.blog.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin Dashboard Controller
 */
@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public Result<DashboardStatsDTO> getStats() {
        return Result.success(dashboardService.getStats());
    }

    @GetMapping("/hot-articles")
    public Result<List<ArticleDTO>> getHotArticles(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(dashboardService.getHotArticles(limit));
    }

    @GetMapping("/recent-comments")
    public Result<List<CommentDTO>> getRecentComments(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(dashboardService.getRecentComments(limit));
    }
}