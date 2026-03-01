package com.blog.domain.repository;

import com.blog.domain.model.Comment;

import java.util.List;
import java.util.Optional;

/**
 * Comment Repository Interface
 */
public interface CommentRepository {

    /**
     * Save comment
     */
    Comment save(Comment comment);

    /**
     * Update comment
     */
    Comment update(Comment comment);

    /**
     * Find by ID
     */
    Optional<Comment> findById(Long id);

    /**
     * Find comments by article ID
     */
    List<Comment> findByArticleId(Long articleId);

    /**
     * Find comments by user ID
     */
    List<Comment> findByUserId(Long userId, int page, int size);

    /**
     * Find pending comments (for review)
     */
    List<Comment> findPending(int page, int size);

    /**
     * Find recent comments
     */
    List<Comment> findRecent(int limit);

    /**
     * Count comments by article
     */
    long countByArticleId(Long articleId);

    /**
     * Count pending comments
     */
    long countPending();

    /**
     * Delete by ID
     */
    void deleteById(Long id);

    /**
     * Update status
     */
    void updateStatus(Long id, String status);

    /**
     * Update comment count for article
     */
    void updateArticleCommentCount(Long articleId);
}