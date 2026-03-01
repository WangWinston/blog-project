package com.blog.domain.repository;

import com.blog.domain.model.Category;

import java.util.List;
import java.util.Optional;

/**
 * Category Repository Interface
 */
public interface CategoryRepository {

    /**
     * Save category
     */
    Category save(Category category);

    /**
     * Update category
     */
    Category update(Category category);

    /**
     * Find by ID
     */
    Optional<Category> findById(Long id);

    /**
     * Find by slug
     */
    Optional<Category> findBySlug(String slug);

    /**
     * Find all categories
     */
    List<Category> findAll();

    /**
     * Find root categories (no parent)
     */
    List<Category> findRootCategories();

    /**
     * Find children by parent ID
     */
    List<Category> findByParentId(Long parentId);

    /**
     * Find category tree
     */
    List<Category> findTree();

    /**
     * Check if name exists
     */
    boolean existsByName(String name);

    /**
     * Check if slug exists
     */
    boolean existsBySlug(String slug);

    /**
     * Delete by ID
     */
    void deleteById(Long id);

    /**
     * Update article count
     */
    void updateArticleCount(Long id, int delta);
}