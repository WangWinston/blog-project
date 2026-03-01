package com.blog.domain.repository;

import com.blog.domain.model.UserFavorite;

import java.util.List;
import java.util.Optional;

/**
 * User Favorite Repository Interface
 */
public interface UserFavoriteRepository {

    /**
     * Save favorite
     */
    UserFavorite save(UserFavorite userFavorite);

    /**
     * Find by user and article
     */
    Optional<UserFavorite> findByUserIdAndArticleId(Long userId, Long articleId);

    /**
     * Check if user favorited article
     */
    boolean existsByUserIdAndArticleId(Long userId, Long articleId);

    /**
     * Find all favorited articles by user
     */
    List<Long> findArticleIdsByUserId(Long userId, int page, int size);

    /**
     * Count favorites by user
     */
    long countByUserId(Long userId);

    /**
     * Delete favorite
     */
    void deleteByUserIdAndArticleId(Long userId, Long articleId);

    /**
     * Count favorites by article
     */
    long countByArticleId(Long articleId);
}