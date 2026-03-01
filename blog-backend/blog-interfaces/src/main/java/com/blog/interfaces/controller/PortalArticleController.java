package com.blog.interfaces.controller;

import com.blog.application.dto.ArticleDTO;
import com.blog.application.dto.PageResultDTO;
import com.blog.application.service.ArticleService;
import com.blog.application.service.InteractionService;
import com.blog.common.Result;
import com.blog.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Portal Article Controller
 */
@RestController
@RequestMapping("/api/portal/articles")
@RequiredArgsConstructor
public class PortalArticleController {

    private final ArticleService articleService;
    private final InteractionService interactionService;

    @GetMapping
    public Result<PageResultDTO<ArticleDTO>> getPublishedArticles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ArticleDTO> articles = articleService.getPublishedArticles(page, size);
        return Result.success(PageResultDTO.of(articles, (long) articles.size(), page, size));
    }

    @GetMapping("/{id}")
    public Result<ArticleDTO> getArticle(@PathVariable Long id,
                                         @AuthenticationPrincipal UserPrincipal principal) {
        ArticleDTO article = articleService.getArticleById(id);
        articleService.incrementViewCount(id);

        // Check if user liked/favorited
        if (principal != null) {
            article.setIsLiked(interactionService.isLiked(id, principal.getUserId()));
            article.setIsFavorited(interactionService.isFavorited(id, principal.getUserId()));
        }

        return Result.success(article);
    }

    @GetMapping("/hot")
    public Result<List<ArticleDTO>> getHotArticles(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(articleService.getHotArticles(limit));
    }

    @GetMapping("/category/{categoryId}")
    public Result<PageResultDTO<ArticleDTO>> getArticlesByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ArticleDTO> articles = articleService.getArticlesByCategory(categoryId, page, size);
        return Result.success(PageResultDTO.of(articles, (long) articles.size(), page, size));
    }

    @GetMapping("/tag/{tagId}")
    public Result<PageResultDTO<ArticleDTO>> getArticlesByTag(
            @PathVariable Long tagId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ArticleDTO> articles = articleService.getArticlesByTag(tagId, page, size);
        return Result.success(PageResultDTO.of(articles, (long) articles.size(), page, size));
    }

    @GetMapping("/search")
    public Result<PageResultDTO<ArticleDTO>> searchArticles(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ArticleDTO> articles = articleService.searchArticles(keyword, page, size);
        return Result.success(PageResultDTO.of(articles, (long) articles.size(), page, size));
    }
}