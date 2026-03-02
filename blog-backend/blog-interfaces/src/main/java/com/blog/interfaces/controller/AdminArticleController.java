package com.blog.interfaces.controller;

import com.blog.application.dto.*;
import com.blog.application.service.ArticleService;
import com.blog.common.Result;
import com.blog.infrastructure.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Admin Article Controller
 */
@Tag(name = "Admin - Articles", description = "Article management API for administrators")
@RestController
@RequestMapping("/api/admin/articles")
@RequiredArgsConstructor
@Validated
public class AdminArticleController {

    private final ArticleService articleService;

    @Operation(summary = "Get article list", description = "Get paginated list of articles with optional filters")
    @GetMapping
    public Result<List<ArticleDTO>> getAllArticles(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Article status filter") @RequestParam(required = false) String status,
            @Parameter(description = "Category ID filter") @RequestParam(required = false) Long categoryId) {
        return Result.success(articleService.getPublishedArticles(page, size));
    }

    @Operation(summary = "Get article by ID", description = "Get article details by ID")
    @GetMapping("/{id}")
    public Result<ArticleDTO> getArticle(
            @Parameter(description = "Article ID") @PathVariable Long id) {
        return Result.success(articleService.getArticleById(id));
    }

    @Operation(summary = "Create article", description = "Create a new article (draft)")
    @PostMapping
    public Result<ArticleDTO> createArticle(
            @Valid @RequestBody ArticleDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        return Result.success(articleService.createArticle(dto, principal.getUserId()));
    }

    @Operation(summary = "Update article", description = "Update an existing article")
    @PutMapping("/{id}")
    public Result<ArticleDTO> updateArticle(
            @Parameter(description = "Article ID") @PathVariable Long id,
            @Valid @RequestBody ArticleDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        return Result.success(articleService.updateArticle(id, dto, principal.getUserId()));
    }

    @Operation(summary = "Delete article", description = "Soft delete an article")
    @DeleteMapping("/{id}")
    public Result<Void> deleteArticle(
            @Parameter(description = "Article ID") @PathVariable Long id) {
        articleService.deleteArticle(id);
        return Result.success();
    }

    @Operation(summary = "Publish article", description = "Publish a draft article")
    @PostMapping("/{id}/publish")
    public Result<ArticleDTO> publishArticle(
            @Parameter(description = "Article ID") @PathVariable Long id) {
        return Result.success(articleService.publishArticle(id));
    }

    @Operation(summary = "Unpublish article", description = "Unpublish a published article")
    @PostMapping("/{id}/unpublish")
    public Result<ArticleDTO> unpublishArticle(
            @Parameter(description = "Article ID") @PathVariable Long id) {
        return Result.success(articleService.unpublishArticle(id));
    }

    @Operation(summary = "Schedule article", description = "Schedule article for future publishing")
    @PostMapping("/{id}/schedule")
    public Result<Void> scheduleArticle(
            @Parameter(description = "Article ID") @PathVariable Long id,
            @Parameter(description = "Scheduled publish time") @RequestParam LocalDateTime publishTime) {
        articleService.scheduleArticle(id, publishTime);
        return Result.success();
    }

    @Operation(summary = "Get article versions", description = "Get version history of an article")
    @GetMapping("/{id}/versions")
    public Result<List<ArticleVersionDTO>> getArticleVersions(
            @Parameter(description = "Article ID") @PathVariable Long id) {
        return Result.success(articleService.getArticleVersions(id));
    }

    @Operation(summary = "Rollback to version", description = "Rollback article to a specific version")
    @PostMapping("/{id}/rollback")
    public Result<ArticleDTO> rollbackToVersion(
            @Parameter(description = "Article ID") @PathVariable Long id,
            @Parameter(description = "Version number") @RequestParam Integer version,
            @AuthenticationPrincipal UserPrincipal principal) {
        return Result.success(articleService.rollbackToVersion(id, version, principal.getUserId()));
    }
}