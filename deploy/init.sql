-- Blog Database Schema
-- MySQL 8.0+

CREATE DATABASE IF NOT EXISTS blog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE blog;

-- User Table
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'User ID',
    `username` VARCHAR(50) NOT NULL COMMENT 'Username',
    `password` VARCHAR(255) DEFAULT NULL COMMENT 'Password (encrypted)',
    `email` VARCHAR(100) DEFAULT NULL COMMENT 'Email',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT 'Avatar URL',
    `role` VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT 'Role (ADMIN, USER)',
    `github_id` VARCHAR(50) DEFAULT NULL COMMENT 'GitHub ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status (0: disabled, 1: enabled)',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_github_id` (`github_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User Table';

-- Profile Table
CREATE TABLE IF NOT EXISTS `profile` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Profile ID',
    `user_id` BIGINT NOT NULL COMMENT 'User ID',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT 'Nickname',
    `bio` VARCHAR(500) DEFAULT NULL COMMENT 'Bio/Introduction',
    `website` VARCHAR(200) DEFAULT NULL COMMENT 'Personal website',
    `location` VARCHAR(100) DEFAULT NULL COMMENT 'Location',
    `github_username` VARCHAR(50) DEFAULT NULL COMMENT 'GitHub username',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    CONSTRAINT `fk_profile_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User Profile Table';

-- Category Table
CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Category ID',
    `name` VARCHAR(50) NOT NULL COMMENT 'Category name',
    `slug` VARCHAR(50) NOT NULL COMMENT 'Slug (URL-friendly)',
    `description` VARCHAR(200) DEFAULT NULL COMMENT 'Description',
    `parent_id` BIGINT DEFAULT NULL COMMENT 'Parent category ID',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT 'Sort order',
    `icon` VARCHAR(50) DEFAULT NULL COMMENT 'Icon',
    `article_count` INT NOT NULL DEFAULT 0 COMMENT 'Article count',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_slug` (`slug`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Category Table';

-- Tag Table
CREATE TABLE IF NOT EXISTS `tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Tag ID',
    `name` VARCHAR(50) NOT NULL COMMENT 'Tag name',
    `slug` VARCHAR(50) NOT NULL COMMENT 'Slug (URL-friendly)',
    `description` VARCHAR(200) DEFAULT NULL COMMENT 'Description',
    `color` VARCHAR(10) DEFAULT '#3B82F6' COMMENT 'Tag color (hex)',
    `article_count` INT NOT NULL DEFAULT 0 COMMENT 'Article count',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_slug` (`slug`),
    KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tag Table';

-- Article Table
CREATE TABLE IF NOT EXISTS `article` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Article ID',
    `title` VARCHAR(200) NOT NULL COMMENT 'Title',
    `slug` VARCHAR(200) NOT NULL COMMENT 'Slug (URL-friendly)',
    `summary` VARCHAR(500) DEFAULT NULL COMMENT 'Summary/Excerpt',
    `content` LONGTEXT COMMENT 'Content (Markdown)',
    `cover_image` VARCHAR(500) DEFAULT NULL COMMENT 'Cover image URL',
    `category_id` BIGINT DEFAULT NULL COMMENT 'Category ID',
    `author_id` BIGINT NOT NULL COMMENT 'Author ID',
    `status` VARCHAR(20) NOT NULL DEFAULT 'DRAFT' COMMENT 'Status (DRAFT, PUBLISHED, UNPUBLISHED, SCHEDULED)',
    `is_top` TINYINT NOT NULL DEFAULT 0 COMMENT 'Is top/pinned (0: no, 1: yes)',
    `is_recommend` TINYINT NOT NULL DEFAULT 0 COMMENT 'Is recommended (0: no, 1: yes)',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT 'View count',
    `like_count` INT NOT NULL DEFAULT 0 COMMENT 'Like count',
    `comment_count` INT NOT NULL DEFAULT 0 COMMENT 'Comment count',
    `scheduled_publish_time` DATETIME DEFAULT NULL COMMENT 'Scheduled publish time',
    `published_time` DATETIME DEFAULT NULL COMMENT 'Published time',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_slug` (`slug`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_author_id` (`author_id`),
    KEY `idx_status` (`status`),
    KEY `idx_published_time` (`published_time`),
    KEY `idx_is_top` (`is_top`),
    FULLTEXT KEY `ft_title_content` (`title`, `content`) WITH PARSER ngram,
    CONSTRAINT `fk_article_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL,
    CONSTRAINT `fk_article_author` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Article Table';

-- Article Tag Relation Table
CREATE TABLE IF NOT EXISTS `article_tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Relation ID',
    `article_id` BIGINT NOT NULL COMMENT 'Article ID',
    `tag_id` BIGINT NOT NULL COMMENT 'Tag ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_article_tag` (`article_id`, `tag_id`),
    KEY `idx_tag_id` (`tag_id`),
    CONSTRAINT `fk_article_tag_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_article_tag_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Article Tag Relation Table';

-- Comment Table
CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Comment ID',
    `article_id` BIGINT NOT NULL COMMENT 'Article ID',
    `user_id` BIGINT NOT NULL COMMENT 'User ID',
    `parent_id` BIGINT DEFAULT NULL COMMENT 'Parent comment ID (for nested replies)',
    `root_id` BIGINT DEFAULT NULL COMMENT 'Root comment ID (for nested replies)',
    `reply_to_user_id` BIGINT DEFAULT NULL COMMENT 'Reply to user ID',
    `content` TEXT NOT NULL COMMENT 'Comment content',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'Status (PENDING, APPROVED, SPAM, DELETED)',
    `like_count` INT NOT NULL DEFAULT 0 COMMENT 'Like count',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP address',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT 'User agent',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    PRIMARY KEY (`id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_root_id` (`root_id`),
    KEY `idx_status` (`status`),
    CONSTRAINT `fk_comment_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Comment Table';

-- Article Version Table
CREATE TABLE IF NOT EXISTS `article_version` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Version ID',
    `article_id` BIGINT NOT NULL COMMENT 'Article ID',
    `version` INT NOT NULL COMMENT 'Version number',
    `title` VARCHAR(200) NOT NULL COMMENT 'Title at this version',
    `content` LONGTEXT COMMENT 'Content at this version',
    `change_log` VARCHAR(500) DEFAULT NULL COMMENT 'Change log',
    `editor_id` BIGINT NOT NULL COMMENT 'Editor ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_article_version` (`article_id`, `version`),
    KEY `idx_article_id` (`article_id`),
    CONSTRAINT `fk_version_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_version_editor` FOREIGN KEY (`editor_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Article Version Table';

-- Scheduled Article Table
CREATE TABLE IF NOT EXISTS `scheduled_article` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Scheduled article ID',
    `article_id` BIGINT NOT NULL COMMENT 'Article ID',
    `scheduled_time` DATETIME NOT NULL COMMENT 'Scheduled publish time',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'Status (PENDING, PUBLISHED, FAILED)',
    `error_message` VARCHAR(500) DEFAULT NULL COMMENT 'Error message (if failed)',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    PRIMARY KEY (`id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_scheduled_time` (`scheduled_time`),
    KEY `idx_status` (`status`),
    CONSTRAINT `fk_scheduled_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Scheduled Article Table';

-- User Like Table
CREATE TABLE IF NOT EXISTS `user_like` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Like ID',
    `user_id` BIGINT NOT NULL COMMENT 'User ID',
    `article_id` BIGINT NOT NULL COMMENT 'Article ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_article` (`user_id`, `article_id`),
    KEY `idx_article_id` (`article_id`),
    CONSTRAINT `fk_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_like_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User Like Table';

-- User Favorite Table
CREATE TABLE IF NOT EXISTS `user_favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Favorite ID',
    `user_id` BIGINT NOT NULL COMMENT 'User ID',
    `article_id` BIGINT NOT NULL COMMENT 'Article ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_article` (`user_id`, `article_id`),
    KEY `idx_article_id` (`article_id`),
    CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_favorite_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User Favorite Table';

-- Insert default admin user (password: admin123)
INSERT INTO `user` (`username`, `password`, `email`, `role`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'admin@blog.com', 'ADMIN', 1);

-- Insert default categories
INSERT INTO `category` (`name`, `slug`, `description`, `sort_order`) VALUES
('技术', 'tech', '技术相关文章', 1),
('生活', 'life', '生活随笔', 2),
('教程', 'tutorial', '教程文档', 3);

-- Insert default tags
INSERT INTO `tag` (`name`, `slug`, `color`) VALUES
('Java', 'java', '#f89820'),
('Spring', 'spring', '#6db33f'),
('Vue', 'vue', '#42b883'),
('JavaScript', 'javascript', '#f7df1e'),
('MySQL', 'mysql', '#4479a1'),
('Redis', 'redis', '#dc382d');