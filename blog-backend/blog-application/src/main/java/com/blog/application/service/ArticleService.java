package com.blog.application.service;

import com.blog.application.dto.*;
import com.blog.common.exception.BlogException;
import com.blog.domain.model.Article;
import com.blog.domain.model.ArticleVersion;
import com.blog.domain.model.Content;
import com.blog.domain.repository.*;
import com.blog.infrastructure.cache.ArticleCacheService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Article Application Service
 */
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ArticleVersionRepository articleVersionRepository;
    private final UserRepository userRepository;
    private final ArticleCacheService articleCacheService;

    @Transactional
    public ArticleDTO createArticle(ArticleDTO dto, Long authorId) {
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setSlug(generateSlug(dto.getTitle()));
        article.setSummary(dto.getSummary());
        article.setContent(new Content(dto.getContent()));
        article.setCoverImage(dto.getCoverImage());
        article.setCategoryId(dto.getCategoryId());
        article.setAuthorId(authorId);
        article.setStatus("DRAFT");
        article.setIsTop(false);
        article.setIsRecommend(false);
        article.setViewCount(0);
        article.setLikeCount(0);
        article.setCommentCount(0);

        if (dto.getTagIds() != null) {
            article.setTagIds(dto.getTagIds());
        }

        Article saved = articleRepository.save(article);
        saveVersion(saved, authorId, "Initial version");

        return toArticleDTO(saved);
    }

    @Transactional
    public ArticleDTO updateArticle(Long id, ArticleDTO dto, Long editorId) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BlogException(404, "Article not found"));

        article.setTitle(dto.getTitle());
        article.setSummary(dto.getSummary());
        article.setContent(new Content(dto.getContent()));
        article.setCoverImage(dto.getCoverImage());
        article.setCategoryId(dto.getCategoryId());

        if (dto.getTagIds() != null) {
            article.setTagIds(dto.getTagIds());
        }

        Article updated = articleRepository.update(article);
        saveVersion(updated, editorId, "Updated");

        // Clear cache
        articleCacheService.deleteArticleDetail(id);

        return toArticleDTO(updated);
    }

    @Transactional
    public ArticleDTO publishArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BlogException(404, "Article not found"));

        if (!"DRAFT".equals(article.getStatus()) && !"UNPUBLISHED".equals(article.getStatus())) {
            throw new BlogException(400, "Article cannot be published");
        }

        article.setStatus("PUBLISHED");
        article.setPublishedTime(LocalDateTime.now());

        Article updated = articleRepository.update(article);
        articleCacheService.deleteArticleDetail(id);

        return toArticleDTO(updated);
    }

    @Transactional
    public ArticleDTO unpublishArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BlogException(404, "Article not found"));

        if (!"PUBLISHED".equals(article.getStatus())) {
            throw new BlogException(400, "Article is not published");
        }

        article.setStatus("UNPUBLISHED");
        Article updated = articleRepository.update(article);

        articleCacheService.deleteArticleDetail(id);

        return toArticleDTO(updated);
    }

    @Transactional
    public void scheduleArticle(Long id, LocalDateTime publishTime) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BlogException(404, "Article not found"));

        if (!"DRAFT".equals(article.getStatus())) {
            throw new BlogException(400, "Only draft articles can be scheduled");
        }

        article.setStatus("SCHEDULED");
        article.setScheduledPublishTime(publishTime);
        articleRepository.update(article);
    }

    @Transactional
    public ArticleDTO rollbackToVersion(Long articleId, Integer version, Long editorId) {
        ArticleVersion articleVersion = articleVersionRepository.findByArticleIdAndVersion(articleId, version)
                .orElseThrow(() -> new BlogException(404, "Version not found"));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BlogException(404, "Article not found"));

        article.setTitle(articleVersion.getTitle());
        article.setContent(new Content(articleVersion.getContent()));
        Article updated = articleRepository.update(article);

        saveVersion(updated, editorId, "Rollback to version " + version);

        articleCacheService.deleteArticleDetail(articleId);

        return toArticleDTO(updated);
    }

    @Transactional
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
        articleCacheService.deleteArticleDetail(id);
    }

    public ArticleDTO getArticleById(Long id) {
        // Try cache first
        Object cached = articleCacheService.getArticleDetail(id);
        if (cached != null) {
            return (ArticleDTO) cached;
        }

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BlogException(404, "Article not found"));

        ArticleDTO dto = toArticleDTO(article);
        articleCacheService.cacheArticleDetail(id, dto);

        return dto;
    }

    public List<ArticleDTO> getPublishedArticles(int page, int size) {
        List<Article> articles = articleRepository.findPublished(page, size);
        return articles.stream()
                .map(this::toArticleDTO)
                .collect(Collectors.toList());
    }

    public List<ArticleDTO> getArticlesByCategory(Long categoryId, int page, int size) {
        List<Article> articles = articleRepository.findByCategoryId(categoryId, page, size);
        return articles.stream()
                .map(this::toArticleDTO)
                .collect(Collectors.toList());
    }

    public List<ArticleDTO> getArticlesByTag(Long tagId, int page, int size) {
        List<Article> articles = articleRepository.findByTagId(tagId, page, size);
        return articles.stream()
                .map(this::toArticleDTO)
                .collect(Collectors.toList());
    }

    public List<ArticleDTO> searchArticles(String keyword, int page, int size) {
        List<Article> articles = articleRepository.search(keyword, page, size);
        return articles.stream()
                .map(this::toArticleDTO)
                .collect(Collectors.toList());
    }

    public List<ArticleDTO> getHotArticles(int limit) {
        // Try cache first
        Object cached = articleCacheService.getHotArticles();
        if (cached != null) {
            @SuppressWarnings("unchecked")
            List<ArticleDTO> result = (List<ArticleDTO>) cached;
            return result;
        }

        List<Article> articles = articleRepository.findHotArticles(limit);
        List<ArticleDTO> dtos = articles.stream()
                .map(this::toArticleDTO)
                .collect(Collectors.toList());

        articleCacheService.cacheHotArticles(dtos);

        return dtos;
    }

    public List<ArticleVersionDTO> getArticleVersions(Long articleId) {
        List<ArticleVersion> versions = articleVersionRepository.findByArticleId(articleId);
        return versions.stream()
                .map(this::toArticleVersionDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void incrementViewCount(Long id) {
        articleRepository.incrementViewCount(id);
        articleCacheService.incrementViewCount(id);
    }

    private void saveVersion(Article article, Long editorId, String changeLog) {
        int version = articleVersionRepository.countByArticleId(article.getId()) + 1;

        ArticleVersion articleVersion = new ArticleVersion();
        articleVersion.setArticleId(article.getId());
        articleVersion.setVersion(version);
        articleVersion.setTitle(article.getTitle());
        articleVersion.setContent(article.getContent().getMarkdown());
        articleVersion.setChangeLog(changeLog);
        articleVersion.setEditorId(editorId);

        articleVersionRepository.save(articleVersion);
    }

    private String generateSlug(String title) {
        if (StringUtils.isBlank(title)) {
            return "article-" + System.currentTimeMillis();
        }
        String slug = title.toLowerCase()
                .replaceAll("[^a-z0-9\\u4e00-\\u9fa5]+", "-")
                .replaceAll("^-|-$", "");
        return StringUtils.isNotBlank(slug) ? slug : "article-" + System.currentTimeMillis();
    }

    private ArticleDTO toArticleDTO(Article article) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setSlug(article.getSlug());
        dto.setSummary(article.getSummary());
        dto.setContent(article.getContent() != null ? article.getContent().getMarkdown() : null);
        dto.setCoverImage(article.getCoverImage());
        dto.setCategoryId(article.getCategoryId());
        dto.setAuthorId(article.getAuthorId());
        dto.setStatus(article.getStatus());
        dto.setIsTop(article.getIsTop());
        dto.setIsRecommend(article.getIsRecommend());
        dto.setViewCount(article.getViewCount());
        dto.setLikeCount(article.getLikeCount());
        dto.setCommentCount(article.getCommentCount());
        dto.setScheduledPublishTime(article.getScheduledPublishTime());
        dto.setPublishedTime(article.getPublishedTime());
        dto.setCreatedTime(article.getCreatedTime());
        dto.setUpdatedTime(article.getUpdatedTime());
        dto.setReadTime(article.getContent() != null ? article.getContent().getReadTime() : 0);

        // Load category name
        if (article.getCategoryId() != null) {
            categoryRepository.findById(article.getCategoryId())
                    .ifPresent(cat -> dto.setCategoryName(cat.getName()));
        }

        // Load author name
        if (article.getAuthorId() != null) {
            userRepository.findById(article.getAuthorId())
                    .ifPresent(user -> dto.setAuthorName(user.getUsername()));
        }

        // Load tags
        if (article.getTagIds() != null && !article.getTagIds().isEmpty()) {
            dto.setTagIds(article.getTagIds());
            dto.setTags(article.getTagIds().stream()
                    .map(tagId -> tagRepository.findById(tagId))
                    .filter(opt -> opt.isPresent())
                    .map(opt -> toTagDTO(opt.get()))
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private TagDTO toTagDTO(com.blog.domain.model.Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setSlug(tag.getSlug());
        dto.setDescription(tag.getDescription());
        dto.setColor(tag.getColor());
        dto.setArticleCount(tag.getArticleCount());
        return dto;
    }

    private ArticleVersionDTO toArticleVersionDTO(ArticleVersion version) {
        ArticleVersionDTO dto = new ArticleVersionDTO();
        dto.setId(version.getId());
        dto.setArticleId(version.getArticleId());
        dto.setVersion(version.getVersion());
        dto.setTitle(version.getTitle());
        dto.setContent(version.getContent());
        dto.setChangeLog(version.getChangeLog());
        dto.setEditorId(version.getEditorId());
        dto.setCreatedTime(version.getCreatedTime());

        if (version.getEditorId() != null) {
            userRepository.findById(version.getEditorId())
                    .ifPresent(user -> dto.setEditorName(user.getUsername()));
        }

        return dto;
    }
}