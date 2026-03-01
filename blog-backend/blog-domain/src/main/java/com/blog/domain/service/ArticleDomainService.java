package com.blog.domain.service;

import com.blog.domain.model.Article;
import com.blog.domain.repository.ArticleRepository;

import java.util.List;

/**
 * Article Domain Service
 */
public class ArticleDomainService {

    private final ArticleRepository articleRepository;

    public ArticleDomainService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    /**
     * Calculate article statistics
     */
    public ArticleStatistics calculateStatistics() {
        ArticleStatistics stats = new ArticleStatistics();
        stats.setTotalPublished(articleRepository.countPublished());
        // Additional statistics would be calculated here
        return stats;
    }

    /**
     * Build article summary from content
     */
    public String buildSummary(String content, int maxLength) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        // Remove markdown syntax
        String summary = content
                .replaceAll("#+\\s*", "")
                .replaceAll("\\*{1,2}([^*]+)\\*{1,2}", "$1")
                .replaceAll("`{1,3}[^`]+`{1,3}", "")
                .replaceAll("!\\[([^]]*)]\\([^)]+\\)", "")
                .replaceAll("\\[([^]]+)]\\([^)]+\\)", "$1")
                .replaceAll("\\s+", " ")
                .trim();

        if (summary.length() <= maxLength) {
            return summary;
        }
        return summary.substring(0, maxLength) + "...";
    }

    /**
     * Generate slug from title
     */
    public String generateSlug(String title) {
        if (title == null || title.isEmpty()) {
            return "";
        }
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\u4e00-\\u9fa5]+", "-")
                .replaceAll("^-|-$", "");
    }

    /**
     * Article Statistics VO
     */
    public static class ArticleStatistics {
        private long totalPublished;
        private long totalDrafts;
        private long totalViews;
        private long totalLikes;

        public long getTotalPublished() {
            return totalPublished;
        }

        public void setTotalPublished(long totalPublished) {
            this.totalPublished = totalPublished;
        }

        public long getTotalDrafts() {
            return totalDrafts;
        }

        public void setTotalDrafts(long totalDrafts) {
            this.totalDrafts = totalDrafts;
        }

        public long getTotalViews() {
            return totalViews;
        }

        public void setTotalViews(long totalViews) {
            this.totalViews = totalViews;
        }

        public long getTotalLikes() {
            return totalLikes;
        }

        public void setTotalLikes(long totalLikes) {
            this.totalLikes = totalLikes;
        }
    }
}