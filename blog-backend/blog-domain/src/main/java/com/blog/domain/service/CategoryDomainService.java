package com.blog.domain.service;

import com.blog.domain.model.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Category Domain Service
 */
public class CategoryDomainService {

    /**
     * Build category tree from flat list
     */
    public List<Category> buildTree(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Long, Category> categoryMap = new HashMap<>();
        List<Category> rootCategories = new ArrayList<>();

        // First pass: create map
        for (Category category : categories) {
            categoryMap.put(category.getId(), category);
            category.setChildren(new ArrayList<>());
        }

        // Second pass: build tree
        for (Category category : categories) {
            if (category.isRoot()) {
                rootCategories.add(category);
            } else {
                Category parent = categoryMap.get(category.getParentId());
                if (parent != null) {
                    parent.addChild(category);
                }
            }
        }

        return rootCategories;
    }

    /**
     * Get all descendant category IDs
     */
    public List<Long> getDescendantIds(List<Category> allCategories, Long parentId) {
        List<Long> ids = new ArrayList<>();
        ids.add(parentId);

        collectDescendantIds(allCategories, parentId, ids);
        return ids;
    }

    private void collectDescendantIds(List<Category> categories, Long parentId, List<Long> ids) {
        for (Category category : categories) {
            if (parentId.equals(category.getParentId())) {
                ids.add(category.getId());
                collectDescendantIds(categories, category.getId(), ids);
            }
        }
    }

    /**
     * Calculate category level in tree
     */
    public int calculateLevel(List<Category> allCategories, Long categoryId) {
        Category category = allCategories.stream()
                .filter(c -> c.getId().equals(categoryId))
                .findFirst()
                .orElse(null);

        if (category == null || category.isRoot()) {
            return 0;
        }

        int level = 0;
        Long currentParentId = category.getParentId();
        while (currentParentId != null && currentParentId != 0) {
            level++;
            final Long parentId = currentParentId;
            Category parent = allCategories.stream()
                    .filter(c -> c.getId().equals(parentId))
                    .findFirst()
                    .orElse(null);
            if (parent == null) {
                break;
            }
            currentParentId = parent.getParentId();
        }

        return level;
    }
}