package com.blog.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.blog.domain.model.Category;
import com.blog.domain.repository.CategoryRepository;
import com.blog.infrastructure.persistence.mapper.CategoryMapper;
import com.blog.infrastructure.persistence.po.CategoryPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Category Repository Implementation
 */
@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryMapper categoryMapper;

    @Override
    public Category save(Category category) {
        CategoryPO po = toCategoryPO(category);
        categoryMapper.insert(po);
        category.setId(po.getId());
        return category;
    }

    @Override
    public Category update(Category category) {
        CategoryPO po = toCategoryPO(category);
        categoryMapper.updateById(po);
        return category;
    }

    @Override
    public Optional<Category> findById(Long id) {
        CategoryPO po = categoryMapper.selectById(id);
        return po != null ? Optional.of(toCategory(po)) : Optional.empty();
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        LambdaQueryWrapper<CategoryPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryPO::getSlug, slug);
        CategoryPO po = categoryMapper.selectOne(wrapper);
        return po != null ? Optional.of(toCategory(po)) : Optional.empty();
    }

    @Override
    public List<Category> findAll() {
        return categoryMapper.selectList(null).stream()
                .map(this::toCategory)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findRootCategories() {
        LambdaQueryWrapper<CategoryPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(CategoryPO::getParentId)
                .or()
                .eq(CategoryPO::getParentId, 0)
                .orderByAsc(CategoryPO::getSortOrder);
        return categoryMapper.selectList(wrapper).stream()
                .map(this::toCategory)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findByParentId(Long parentId) {
        LambdaQueryWrapper<CategoryPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryPO::getParentId, parentId)
                .orderByAsc(CategoryPO::getSortOrder);
        return categoryMapper.selectList(wrapper).stream()
                .map(this::toCategory)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findTree() {
        return findAll();
    }

    @Override
    public boolean existsByName(String name) {
        LambdaQueryWrapper<CategoryPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryPO::getName, name);
        return categoryMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean existsBySlug(String slug) {
        LambdaQueryWrapper<CategoryPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryPO::getSlug, slug);
        return categoryMapper.selectCount(wrapper) > 0;
    }

    @Override
    public void deleteById(Long id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public void updateArticleCount(Long id, int delta) {
        LambdaUpdateWrapper<CategoryPO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CategoryPO::getId, id)
                .setSql("article_count = article_count + " + delta);
        categoryMapper.update(null, wrapper);
    }

    private Category toCategory(CategoryPO po) {
        Category category = new Category();
        category.setId(po.getId());
        category.setName(po.getName());
        category.setSlug(po.getSlug());
        category.setDescription(po.getDescription());
        category.setParentId(po.getParentId());
        category.setSortOrder(po.getSortOrder());
        category.setIcon(po.getIcon());
        category.setArticleCount(po.getArticleCount());
        category.setCreatedTime(po.getCreatedTime());
        category.setUpdatedTime(po.getUpdatedTime());
        return category;
    }

    private CategoryPO toCategoryPO(Category category) {
        CategoryPO po = new CategoryPO();
        po.setId(category.getId());
        po.setName(category.getName());
        po.setSlug(category.getSlug());
        po.setDescription(category.getDescription());
        po.setParentId(category.getParentId());
        po.setSortOrder(category.getSortOrder());
        po.setIcon(category.getIcon());
        po.setArticleCount(category.getArticleCount());
        return po;
    }
}