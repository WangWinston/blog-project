package com.blog.application.service;

import com.blog.application.dto.CategoryDTO;
import com.blog.domain.model.Category;
import com.blog.domain.repository.ArticleRepository;
import com.blog.domain.repository.CategoryRepository;
import com.blog.domain.service.CategoryDomainService;
import com.blog.common.exception.BlogException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Category Application Service
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final CategoryDomainService categoryDomainService;

    @Transactional
    public CategoryDTO createCategory(CategoryDTO dto) {
        if (StringUtils.isBlank(dto.getName())) {
            throw new BlogException(400, "Category name is required");
        }

        if (categoryRepository.existsByName(dto.getName())) {
            throw new BlogException(400, "Category name already exists");
        }

        Category category = new Category();
        category.setName(dto.getName());
        category.setSlug(generateSlug(dto.getName()));
        category.setDescription(dto.getDescription());
        category.setParentId(dto.getParentId());
        category.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        category.setIcon(dto.getIcon());
        category.setArticleCount(0);

        Category saved = categoryRepository.save(category);
        return toCategoryDTO(saved);
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BlogException(404, "Category not found"));

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setParentId(dto.getParentId());
        category.setSortOrder(dto.getSortOrder());
        category.setIcon(dto.getIcon());

        Category updated = categoryRepository.update(category);
        return toCategoryDTO(updated);
    }

    @Transactional
    public void deleteCategory(Long id) {
        // Check if category has articles
        long articleCount = articleRepository.countByCategoryId(id);
        if (articleCount > 0) {
            throw new BlogException(400, "Cannot delete category with articles");
        }

        categoryRepository.deleteById(id);
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BlogException(404, "Category not found"));
        return toCategoryDTO(category);
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toCategoryDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getCategoryTree() {
        List<Category> allCategories = categoryRepository.findTree();
        List<Category> tree = categoryDomainService.buildTree(allCategories);
        return tree.stream()
                .map(this::toCategoryDTO)
                .collect(Collectors.toList());
    }

    private String generateSlug(String name) {
        if (StringUtils.isBlank(name)) {
            return "category-" + System.currentTimeMillis();
        }
        String slug = name.toLowerCase()
                .replaceAll("[^a-z0-9\\u4e00-\\u9fa5]+", "-")
                .replaceAll("^-|-$", "");
        return StringUtils.isNotBlank(slug) ? slug : "category-" + System.currentTimeMillis();
    }

    private CategoryDTO toCategoryDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        dto.setParentId(category.getParentId());
        dto.setSortOrder(category.getSortOrder());
        dto.setIcon(category.getIcon());
        dto.setArticleCount(category.getArticleCount());

        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            dto.setChildren(category.getChildren().stream()
                    .map(this::toCategoryDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}