package com.blog.domain.repository;

import com.blog.domain.model.Article;

import java.util.List;
import java.util.Optional;

/**
 * Article Repository Interface
 */
public interface ArticleRepository {

    /**
     * Save article
     */
    Article save(Article article);

    /**
     * Update article
     */
    Article update(Article article);

    /**
     * Find by ID
     */
    Optional<Article> findById(Long id);

    /**
     * Find by slug
     */
    Optional<Article> findBySlug(String slug);

    /**
     * Find all published articles with pagination
     */
    List<Article> findPublished(int page, int size);

    /**
     * Find articles by category ID
     */
    List<Article> findByCategoryId(Long categoryId, int page, int size);

    /**
     * Find articles by tag ID
     */
    List<Article> findByTagId(Long tagId, int page, int size);

    /**
     * Find articles by author ID
     */
    List<Article> findByAuthorId(Long authorId, int page, int size);

    /**
     * Find hot/recommended articles
     */
    List<Article> findHotArticles(int limit);

    /**
     * Find top/pinned articles
     */
    List<Article> findTopArticles(int limit);

    /**
     * Search articles by keyword
     */
    List<Article> search(String keyword, int page, int size);

    /**
     * Count published articles
     */
    long countPublished();

    /**
     * Count articles by category
     */
    long countByCategoryId(Long categoryId);

    /**
     * Count articles by tag
     */
    long countByTagId(Long tagId);

    /**
     * Delete by ID
     */
    void deleteById(Long id);

    /**
     * Update status
     */
    void updateStatus(Long id, String status);

    /**
     * Increment view count
     */
    void incrementViewCount(Long id);

    /**
     * Update like count
     */
    void updateLikeCount(Long id, int delta);
}