package com.blog.domain.repository;

import com.blog.domain.model.ScheduledArticle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Scheduled Article Repository Interface
 */
public interface ScheduledArticleRepository {

    /**
     * Save scheduled article
     */
    ScheduledArticle save(ScheduledArticle scheduledArticle);

    /**
     * Find by ID
     */
    Optional<ScheduledArticle> findById(Long id);

    /**
     * Find by article ID
     */
    Optional<ScheduledArticle> findByArticleId(Long articleId);

    /**
     * Find pending scheduled articles before time
     */
    List<ScheduledArticle> findPendingBeforeTime(LocalDateTime time);

    /**
     * Find all pending scheduled articles
     */
    List<ScheduledArticle> findAllPending();

    /**
     * Update status
     */
    void updateStatus(Long id, String status, String errorMessage);

    /**
     * Delete by article ID
     */
    void deleteByArticleId(Long articleId);
}