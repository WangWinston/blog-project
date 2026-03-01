package com.blog.domain.repository;

import com.blog.domain.model.User;

import java.util.Optional;

/**
 * User Repository Interface
 */
public interface UserRepository {

    /**
     * Save user
     */
    User save(User user);

    /**
     * Update user
     */
    User update(User user);

    /**
     * Find by ID
     */
    Optional<User> findById(Long id);

    /**
     * Find by username
     */
    Optional<User> findByUsername(String username);

    /**
     * Find by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find by GitHub ID
     */
    Optional<User> findByGithubId(String githubId);

    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Delete by ID
     */
    void deleteById(Long id);

    /**
     * Count all users
     */
    long count();
}