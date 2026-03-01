package com.blog.domain.repository;

import com.blog.domain.model.UserLike;

import java.util.List;
import java.util.Optional;

/**
 * User Like Repository Interface
 */
public interface UserLikeRepository {

    /**
     * Save like
     */
    UserLike save(UserLike userLike);

    /**
     * Find by user and article
     */
    Optional<UserLike> findByUserIdAndArticleId(Long userId, Long articleId);

    /**
     * Check if user liked article
     */
    boolean existsByUserIdAndArticleId(Long userId, Long articleId);

    /**
     * Find all liked articles by user
     */
    List<Long> findArticleIdsByUserId(Long userId);

    /**
     * Delete like
     */
    void deleteByUserIdAndArticleId(Long userId, Long articleId);

    /**
     * Count likes by article
     */
    long countByArticleId(Long articleId);
}