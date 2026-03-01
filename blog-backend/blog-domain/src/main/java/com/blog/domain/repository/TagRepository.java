package com.blog.domain.repository;

import com.blog.domain.model.Tag;

import java.util.List;
import java.util.Optional;

/**
 * Tag Repository Interface
 */
public interface TagRepository {

    /**
     * Save tag
     */
    Tag save(Tag tag);

    /**
     * Update tag
     */
    Tag update(Tag tag);

    /**
     * Find by ID
     */
    Optional<Tag> findById(Long id);

    /**
     * Find by slug
     */
    Optional<Tag> findBySlug(String slug);

    /**
     * Find all tags
     */
    List<Tag> findAll();

    /**
     * Find tags by article ID
     */
    List<Tag> findByArticleId(Long articleId);

    /**
     * Find popular tags
     */
    List<Tag> findPopular(int limit);

    /**
     * Check if name exists
     */
    boolean existsByName(String name);

    /**
     * Delete by ID
     */
    void deleteById(Long id);

    /**
     * Update article count
     */
    void updateArticleCount(Long id, int delta);

    /**
     * Add tag to article
     */
    void addToArticle(Long articleId, Long tagId);

    /**
     * Remove tag from article
     */
    void removeFromArticle(Long articleId, Long tagId);

    /**
     * Remove all tags from article
     */
    void removeAllFromArticle(Long articleId);
}