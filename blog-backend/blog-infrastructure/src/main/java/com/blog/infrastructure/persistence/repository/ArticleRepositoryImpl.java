package com.blog.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.domain.model.Article;
import com.blog.domain.model.Content;
import com.blog.domain.repository.ArticleRepository;
import com.blog.infrastructure.persistence.mapper.ArticleMapper;
import com.blog.infrastructure.persistence.mapper.ArticleTagMapper;
import com.blog.infrastructure.persistence.mapper.TagMapper;
import com.blog.infrastructure.persistence.po.ArticlePO;
import com.blog.infrastructure.persistence.po.ArticleTagPO;
import com.blog.infrastructure.persistence.po.TagPO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Article Repository Implementation
 */
@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleMapper articleMapper;
    private final ArticleTagMapper articleTagMapper;
    private final TagMapper tagMapper;

    @Override
    public Article save(Article article) {
        ArticlePO po = toArticlePO(article);
        articleMapper.insert(po);
        article.setId(po.getId());

        if (CollectionUtils.isNotEmpty(article.getTagIds())) {
            saveArticleTags(article.getId(), article.getTagIds());
        }

        return article;
    }

    @Override
    public Article update(Article article) {
        ArticlePO po = toArticlePO(article);
        articleMapper.updateById(po);

        // Update article tags
        deleteArticleTags(article.getId());
        if (CollectionUtils.isNotEmpty(article.getTagIds())) {
            saveArticleTags(article.getId(), article.getTagIds());
        }

        return article;
    }

    @Override
    public Optional<Article> findById(Long id) {
        ArticlePO po = articleMapper.selectById(id);
        if (po == null) {
            return Optional.empty();
        }
        Article article = toArticle(po);
        loadArticleTags(article);
        return Optional.of(article);
    }

    @Override
    public Optional<Article> findBySlug(String slug) {
        LambdaQueryWrapper<ArticlePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticlePO::getSlug, slug);
        ArticlePO po = articleMapper.selectOne(wrapper);
        if (po == null) {
            return Optional.empty();
        }
        Article article = toArticle(po);
        loadArticleTags(article);
        return Optional.of(article);
    }

    @Override
    public List<Article> findPublished(int page, int size) {
        LambdaQueryWrapper<ArticlePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticlePO::getStatus, "PUBLISHED")
                .orderByDesc(ArticlePO::getIsTop)
                .orderByDesc(ArticlePO::getPublishedTime);

        Page<ArticlePO> pageResult = articleMapper.selectPage(new Page<>(page, size), wrapper);
        return pageResult.getRecords().stream()
                .map(this::toArticle)
                .peek(this::loadArticleTags)
                .collect(Collectors.toList());
    }

    @Override
    public List<Article> findByCategoryId(Long categoryId, int page, int size) {
        LambdaQueryWrapper<ArticlePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticlePO::getCategoryId, categoryId)
                .eq(ArticlePO::getStatus, "PUBLISHED")
                .orderByDesc(ArticlePO::getPublishedTime);

        Page<ArticlePO> pageResult = articleMapper.selectPage(new Page<>(page, size), wrapper);
        return pageResult.getRecords().stream()
                .map(this::toArticle)
                .peek(this::loadArticleTags)
                .collect(Collectors.toList());
    }

    @Override
    public List<Article> findByTagId(Long tagId, int page, int size) {
        // Find article IDs by tag
        LambdaQueryWrapper<ArticleTagPO> atWrapper = new LambdaQueryWrapper<>();
        atWrapper.eq(ArticleTagPO::getTagId, tagId);
        List<ArticleTagPO> articleTags = articleTagMapper.selectList(atWrapper);

        if (CollectionUtils.isEmpty(articleTags)) {
            return new ArrayList<>();
        }

        List<Long> articleIds = articleTags.stream()
                .map(ArticleTagPO::getArticleId)
                .collect(Collectors.toList());

        // Find articles by IDs
        LambdaQueryWrapper<ArticlePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ArticlePO::getId, articleIds)
                .eq(ArticlePO::getStatus, "PUBLISHED")
                .orderByDesc(ArticlePO::getPublishedTime);

        Page<ArticlePO> pageResult = articleMapper.selectPage(new Page<>(page, size), wrapper);
        return pageResult.getRecords().stream()
                .map(this::toArticle)
                .peek(this::loadArticleTags)
                .collect(Collectors.toList());
    }

    @Override
    public List<Article> findByAuthorId(Long authorId, int page, int size) {
        LambdaQueryWrapper<ArticlePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticlePO::getAuthorId, authorId)
                .orderByDesc(ArticlePO::getCreatedTime);

        Page<ArticlePO> pageResult = articleMapper.selectPage(new Page<>(page, size), wrapper);
        return pageResult.getRecords().stream()
                .map(this::toArticle)
                .peek(this::loadArticleTags)
                .collect(Collectors.toList());
    }

    @Override
    public List<Article> findHotArticles(int limit) {
        LambdaQueryWrapper<ArticlePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticlePO::getStatus, "PUBLISHED")
                .orderByDesc(ArticlePO::getViewCount)
                .last("LIMIT " + limit);

        return articleMapper.selectList(wrapper).stream()
                .map(this::toArticle)
                .collect(Collectors.toList());
    }

    @Override
    public List<Article> findTopArticles(int limit) {
        LambdaQueryWrapper<ArticlePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticlePO::getStatus, "PUBLISHED")
                .eq(ArticlePO::getIsTop, 1)
                .orderByDesc(ArticlePO::getPublishedTime)
                .last("LIMIT " + limit);

        return articleMapper.selectList(wrapper).stream()
                .map(this::toArticle)
                .collect(Collectors.toList());
    }

    @Override
    public List<Article> search(String keyword, int page, int size) {
        LambdaQueryWrapper<ArticlePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticlePO::getStatus, "PUBLISHED")
                .and(w -> w.like(ArticlePO::getTitle, keyword)
                        .or()
                        .like(ArticlePO::getSummary, keyword)
                        .or()
                        .like(ArticlePO::getContent, keyword))
                .orderByDesc(ArticlePO::getPublishedTime);

        Page<ArticlePO> pageResult = articleMapper.selectPage(new Page<>(page, size), wrapper);
        return pageResult.getRecords().stream()
                .map(this::toArticle)
                .collect(Collectors.toList());
    }

    @Override
    public long countPublished() {
        LambdaQueryWrapper<ArticlePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticlePO::getStatus, "PUBLISHED");
        return articleMapper.selectCount(wrapper);
    }

    @Override
    public long countByCategoryId(Long categoryId) {
        LambdaQueryWrapper<ArticlePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticlePO::getCategoryId, categoryId)
                .eq(ArticlePO::getStatus, "PUBLISHED");
        return articleMapper.selectCount(wrapper);
    }

    @Override
    public long countByTagId(Long tagId) {
        LambdaQueryWrapper<ArticleTagPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTagPO::getTagId, tagId);
        return articleTagMapper.selectCount(wrapper);
    }

    @Override
    public void deleteById(Long id) {
        deleteArticleTags(id);
        articleMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, String status) {
        LambdaUpdateWrapper<ArticlePO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ArticlePO::getId, id)
                .set(ArticlePO::getStatus, status);
        articleMapper.update(null, wrapper);
    }

    @Override
    public void incrementViewCount(Long id) {
        LambdaUpdateWrapper<ArticlePO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ArticlePO::getId, id)
                .setSql("view_count = view_count + 1");
        articleMapper.update(null, wrapper);
    }

    @Override
    public void updateLikeCount(Long id, int delta) {
        LambdaUpdateWrapper<ArticlePO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ArticlePO::getId, id)
                .setSql("like_count = like_count + " + delta);
        articleMapper.update(null, wrapper);
    }

    private Article toArticle(ArticlePO po) {
        Article article = new Article();
        article.setId(po.getId());
        article.setTitle(po.getTitle());
        article.setSlug(po.getSlug());
        article.setSummary(po.getSummary());
        article.setContent(new Content(po.getContent()));
        article.setCoverImage(po.getCoverImage());
        article.setCategoryId(po.getCategoryId());
        article.setAuthorId(po.getAuthorId());
        article.setStatus(po.getStatus());
        article.setIsTop(po.getIsTop() != null && po.getIsTop() == 1);
        article.setIsRecommend(po.getIsRecommend() != null && po.getIsRecommend() == 1);
        article.setViewCount(po.getViewCount());
        article.setLikeCount(po.getLikeCount());
        article.setCommentCount(po.getCommentCount());
        article.setScheduledPublishTime(po.getScheduledPublishTime());
        article.setPublishedTime(po.getPublishedTime());
        article.setCreatedTime(po.getCreatedTime());
        article.setUpdatedTime(po.getUpdatedTime());
        return article;
    }

    private ArticlePO toArticlePO(Article article) {
        ArticlePO po = new ArticlePO();
        po.setId(article.getId());
        po.setTitle(article.getTitle());
        po.setSlug(article.getSlug());
        po.setSummary(article.getSummary());
        po.setContent(article.getContent() != null ? article.getContent().getMarkdown() : null);
        po.setCoverImage(article.getCoverImage());
        po.setCategoryId(article.getCategoryId());
        po.setAuthorId(article.getAuthorId());
        po.setStatus(article.getStatus());
        po.setIsTop(article.getIsTop() != null && article.getIsTop() ? 1 : 0);
        po.setIsRecommend(article.getIsRecommend() != null && article.getIsRecommend() ? 1 : 0);
        po.setViewCount(article.getViewCount());
        po.setLikeCount(article.getLikeCount());
        po.setCommentCount(article.getCommentCount());
        po.setScheduledPublishTime(article.getScheduledPublishTime());
        po.setPublishedTime(article.getPublishedTime());
        return po;
    }

    private void saveArticleTags(Long articleId, List<Long> tagIds) {
        for (Long tagId : tagIds) {
            ArticleTagPO po = new ArticleTagPO();
            po.setArticleId(articleId);
            po.setTagId(tagId);
            articleTagMapper.insert(po);
        }
    }

    private void deleteArticleTags(Long articleId) {
        LambdaQueryWrapper<ArticleTagPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTagPO::getArticleId, articleId);
        articleTagMapper.delete(wrapper);
    }

    private void loadArticleTags(Article article) {
        LambdaQueryWrapper<ArticleTagPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTagPO::getArticleId, article.getId());
        List<ArticleTagPO> articleTags = articleTagMapper.selectList(wrapper);

        if (CollectionUtils.isNotEmpty(articleTags)) {
            List<Long> tagIds = articleTags.stream()
                    .map(ArticleTagPO::getTagId)
                    .collect(Collectors.toList());
            article.setTagIds(tagIds);

            // Load tag names
            List<TagPO> tags = tagMapper.selectBatchIds(tagIds);
            List<String> tagNames = tags.stream()
                    .map(TagPO::getName)
                    .collect(Collectors.toList());
            article.setTagNames(tagNames);
        }
    }
}