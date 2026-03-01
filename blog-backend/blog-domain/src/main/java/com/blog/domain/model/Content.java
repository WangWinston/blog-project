package com.blog.domain.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Article Content Value Object
 */
@Data
public class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Markdown content
     */
    private String markdown;

    /**
     * HTML content (rendered from markdown)
     */
    private String html;

    /**
     * Word count
     */
    private Integer wordCount;

    /**
     * Estimated reading time (minutes)
     */
    private Integer readTime;

    public Content() {
    }

    public Content(String markdown) {
        this.markdown = markdown;
        this.wordCount = calculateWordCount(markdown);
        this.readTime = calculateReadTime(this.wordCount);
    }

    /**
     * Calculate word count (Chinese characters + English words)
     */
    private int calculateWordCount(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        // Remove markdown syntax and count
        String cleanText = text.replaceAll("#+\\s*", "")
                .replaceAll("\\*{1,2}([^*]+)\\*{1,2}", "$1")
                .replaceAll("`{1,3}[^`]+`{1,3}", "")
                .replaceAll("!\\[([^]]*)]\\([^)]+\\)", "")
                .replaceAll("\\[([^]]+)]\\([^)]+\\)", "$1")
                .replaceAll("\\s+", " ")
                .trim();

        return cleanText.length();
    }

    /**
     * Calculate estimated reading time (assume 300 words per minute)
     */
    private int calculateReadTime(int wordCount) {
        if (wordCount <= 0) {
            return 0;
        }
        return Math.max(1, wordCount / 300);
    }
}