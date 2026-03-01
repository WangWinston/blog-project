package com.blog.interfaces.controller;

import com.blog.application.dto.*;
import com.blog.application.service.ArticleService;
import com.blog.common.Result;
import com.blog.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Admin Article Controller
 */
@RestController
@RequestMapping("/api/admin/articles")
@RequiredArgsConstructor
public class AdminArticleController {

    private final ArticleService articleService;

    @GetMapping
    public Result<List<ArticleDTO>> getAllArticles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long categoryId) {
        return Result.success(articleService.getPublishedArticles(page, size));
    }

    @GetMapping("/{id}")
    public Result<ArticleDTO> getArticle(@PathVariable Long id) {
        return Result.success(articleService.getArticleById(id));
    }

    @PostMapping
    public Result<ArticleDTO> createArticle(
            @RequestBody ArticleDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        return Result.success(articleService.createArticle(dto, principal.getUserId()));
    }

    @PutMapping("/{id}")
    public Result<ArticleDTO> updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        return Result.success(articleService.updateArticle(id, dto, principal.getUserId()));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return Result.success();
    }

    @PostMapping("/{id}/publish")
    public Result<ArticleDTO> publishArticle(@PathVariable Long id) {
        return Result.success(articleService.publishArticle(id));
    }

    @PostMapping("/{id}/unpublish")
    public Result<ArticleDTO> unpublishArticle(@PathVariable Long id) {
        return Result.success(articleService.unpublishArticle(id));
    }

    @PostMapping("/{id}/schedule")
    public Result<Void> scheduleArticle(
            @PathVariable Long id,
            @RequestParam LocalDateTime publishTime) {
        articleService.scheduleArticle(id, publishTime);
        return Result.success();
    }

    @GetMapping("/{id}/versions")
    public Result<List<ArticleVersionDTO>> getArticleVersions(@PathVariable Long id) {
        return Result.success(articleService.getArticleVersions(id));
    }

    @PostMapping("/{id}/rollback")
    public Result<ArticleDTO> rollbackToVersion(
            @PathVariable Long id,
            @RequestParam Integer version,
            @AuthenticationPrincipal UserPrincipal principal) {
        return Result.success(articleService.rollbackToVersion(id, version, principal.getUserId()));
    }
}