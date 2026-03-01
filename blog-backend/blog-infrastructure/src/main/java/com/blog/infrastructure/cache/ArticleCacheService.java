package com.blog.infrastructure.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Article Cache Service
 */
@Service
@RequiredArgsConstructor
public class ArticleCacheService {

    private final RedisCacheService redisCacheService;

    private static final String ARTICLE_DETAIL_KEY = "article:detail:";
    private static final String ARTICLE_LIST_KEY = "article:list:page:";
    private static final String ARTICLE_HOT_KEY = "article:hot";
    private static final String ARTICLE_VIEW_COUNT_KEY = "article:view:count";

    private static final long ARTICLE_DETAIL_TTL = 1; // 1 hour
    private static final long ARTICLE_LIST_TTL = 10; // 10 minutes
    private static final long ARTICLE_HOT_TTL = 30; // 30 minutes

    /**
     * Cache article detail
     */
    public void cacheArticleDetail(Long articleId, Object article) {
        String key = ARTICLE_DETAIL_KEY + articleId;
        redisCacheService.set(key, article, ARTICLE_DETAIL_TTL, TimeUnit.HOURS);
    }

    /**
     * Get cached article detail
     */
    public Object getArticleDetail(Long articleId) {
        String key = ARTICLE_DETAIL_KEY + articleId;
        return redisCacheService.get(key);
    }

    /**
     * Delete article detail cache
     */
    public void deleteArticleDetail(Long articleId) {
        String key = ARTICLE_DETAIL_KEY + articleId;
        redisCacheService.delete(key);
    }

    /**
     * Increment view count in cache
     */
    public void incrementViewCount(Long articleId) {
        redisCacheService.hIncrement(ARTICLE_VIEW_COUNT_KEY, String.valueOf(articleId), 1);
    }

    /**
     * Get view count from cache
     */
    public Long getViewCount(Long articleId) {
        Object count = redisCacheService.hGet(ARTICLE_VIEW_COUNT_KEY, String.valueOf(articleId));
        return count != null ? Long.parseLong(count.toString()) : null;
    }

    /**
     * Cache hot articles
     */
    public void cacheHotArticles(Object articles) {
        redisCacheService.set(ARTICLE_HOT_KEY, articles, ARTICLE_HOT_TTL, TimeUnit.MINUTES);
    }

    /**
     * Get cached hot articles
     */
    public Object getHotArticles() {
        return redisCacheService.get(ARTICLE_HOT_KEY);
    }

    /**
     * Delete article list cache
     */
    public void deleteArticleListCache() {
        // In production, you might want to use pattern matching to delete all list caches
        // For simplicity, we'll just note this would need a more sophisticated approach
    }
}