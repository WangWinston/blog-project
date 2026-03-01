package com.blog.common.constant;

/**
 * Security Constants
 */
public final class SecurityConstants {

    private SecurityConstants() {
    }

    /**
     * JWT Header name
     */
    public static final String JWT_HEADER = "Authorization";

    /**
     * JWT Token prefix
     */
    public static final String JWT_PREFIX = "Bearer ";

    /**
     * JWT secret key (should be configured in application.yml)
     */
    public static final String JWT_SECRET_KEY = "blog.jwt.secret";

    /**
     * JWT expiration time (2 hours)
     */
    public static final long JWT_EXPIRATION = 2 * 60 * 60 * 1000L;

    /**
     * JWT refresh expiration time (7 days)
     */
    public static final long JWT_REFRESH_EXPIRATION = 7 * 24 * 60 * 60 * 1000L;

    /**
     * JWT issuer
     */
    public static final String JWT_ISSUER = "blog-system";

    /**
     * Security context attribute
     */
    public static final String SECURITY_CONTEXT = "SECURITY_CONTEXT";

    /**
     * Public API paths (no authentication required)
     */
    public static final String[] PUBLIC_PATHS = {
            "/api/portal/**",
            "/api/auth/**",
            "/api/captcha/**",
            "/actuator/health",
            "/error"
    };

    /**
     * Admin API paths (admin role required)
     */
    public static final String[] ADMIN_PATHS = {
            "/api/admin/**"
    };
}