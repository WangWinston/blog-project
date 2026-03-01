package com.blog.common.constant;

/**
 * Cache Key Constants
 */
public final class CacheConstants {

    private CacheConstants() {
    }

    /**
     * Cache key prefix
     */
    public static final String CACHE_PREFIX = "blog:";

    // ========== Article Cache ==========
    public static final String ARTICLE_DETAIL = CACHE_PREFIX + "article:detail:";
    public static final String ARTICLE_LIST = CACHE_PREFIX + "article:list:page:";
    public static final String ARTICLE_HOT = CACHE_PREFIX + "article:hot";
    public static final String ARTICLE_RELATED = CACHE_PREFIX + "article:related:";
    public static final String ARTICLE_VIEW_COUNT = CACHE_PREFIX + "article:view:count";

    // ========== Category Cache ==========
    public static final String CATEGORY_TREE = CACHE_PREFIX + "category:tree";
    public static final String CATEGORY_DETAIL = CACHE_PREFIX + "category:detail:";

    // ========== Tag Cache ==========
    public static final String TAG_ALL = CACHE_PREFIX + "tag:all";
    public static final String TAG_DETAIL = CACHE_PREFIX + "tag:detail:";

    // ========== User Cache ==========
    public static final String USER_INFO = CACHE_PREFIX + "user:info:";
    public static final String USER_FAVORITE = CACHE_PREFIX + "user:favorite:";

    // ========== Comment Cache ==========
    public static final String COMMENT_ARTICLE = CACHE_PREFIX + "comment:article:";

    // ========== Like Cache ==========
    public static final String ARTICLE_LIKE = CACHE_PREFIX + "article:like:";

    // ========== Captcha Cache ==========
    public static final String CAPTCHA_VERIFIED = CACHE_PREFIX + "captcha:verified:";

    // ========== Default TTL (seconds) ==========
    public static final long TTL_ONE_MINUTE = 60L;
    public static final long TTL_FIVE_MINUTES = 300L;
    public static final long TTL_TEN_MINUTES = 600L;
    public static final long TTL_THIRTY_MINUTES = 1800L;
    public static final long TTL_ONE_HOUR = 3600L;
    public static final long TTL_ONE_DAY = 86400L;
}