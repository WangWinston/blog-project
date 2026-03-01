package com.blog.domain.repository;

import com.blog.domain.model.ArticleVersion;

import java.util.List;
import java.util.Optional;

/**
 * Article Version Repository Interface
 */
public interface ArticleVersionRepository {

    /**
     * Save version
     */
    ArticleVersion save(ArticleVersion version);

    /**
     * Find by ID
     */
    Optional<ArticleVersion> findById(Long id);

    /**
     * Find all versions for an article
     */
    List<ArticleVersion> findByArticleId(Long articleId);

    /**
     * Find latest version for an article
     */
    Optional<ArticleVersion> findLatestByArticleId(Long articleId);

    /**
     * Find specific version for an article
     */
    Optional<ArticleVersion> findByArticleIdAndVersion(Long articleId, Integer version);

    /**
     * Count versions for an article
     */
    int countByArticleId(Long articleId);

    /**
     * Delete by ID
     */
    void deleteById(Long id);
}