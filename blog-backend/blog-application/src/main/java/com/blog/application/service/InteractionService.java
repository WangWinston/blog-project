package com.blog.application.service;

import com.blog.application.dto.ArticleDTO;
import com.blog.domain.model.UserFavorite;
import com.blog.domain.model.UserLike;
import com.blog.domain.repository.ArticleRepository;
import com.blog.domain.repository.UserFavoriteRepository;
import com.blog.domain.repository.UserLikeRepository;
import com.blog.common.exception.BlogException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Interaction Application Service (Like, Favorite)
 */
@Service
@RequiredArgsConstructor
public class InteractionService {

    private final UserLikeRepository userLikeRepository;
    private final UserFavoriteRepository userFavoriteRepository;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    @Transactional
    public boolean toggleLike(Long articleId, Long userId) {
        // Check article exists
        articleRepository.findById(articleId)
                .orElseThrow(() -> new BlogException(404, "Article not found"));

        boolean exists = userLikeRepository.existsByUserIdAndArticleId(userId, articleId);

        if (exists) {
            userLikeRepository.deleteByUserIdAndArticleId(userId, articleId);
            articleRepository.updateLikeCount(articleId, -1);
            return false;
        } else {
            UserLike like = new UserLike();
            like.setUserId(userId);
            like.setArticleId(articleId);
            userLikeRepository.save(like);
            articleRepository.updateLikeCount(articleId, 1);
            return true;
        }
    }

    @Transactional
    public boolean toggleFavorite(Long articleId, Long userId) {
        // Check article exists
        articleRepository.findById(articleId)
                .orElseThrow(() -> new BlogException(404, "Article not found"));

        boolean exists = userFavoriteRepository.existsByUserIdAndArticleId(userId, articleId);

        if (exists) {
            userFavoriteRepository.deleteByUserIdAndArticleId(userId, articleId);
            return false;
        } else {
            UserFavorite favorite = new UserFavorite();
            favorite.setUserId(userId);
            favorite.setArticleId(articleId);
            userFavoriteRepository.save(favorite);
            return true;
        }
    }

    public boolean isLiked(Long articleId, Long userId) {
        return userLikeRepository.existsByUserIdAndArticleId(userId, articleId);
    }

    public boolean isFavorited(Long articleId, Long userId) {
        return userFavoriteRepository.existsByUserIdAndArticleId(userId, articleId);
    }

    public List<ArticleDTO> getUserFavorites(Long userId, int page, int size) {
        List<Long> articleIds = userFavoriteRepository.findArticleIdsByUserId(userId, page, size);
        return articleIds.stream()
                .map(articleService::getArticleById)
                .collect(Collectors.toList());
    }

    public List<ArticleDTO> getUserLikes(Long userId) {
        List<Long> articleIds = userLikeRepository.findArticleIdsByUserId(userId);
        return articleIds.stream()
                .map(articleService::getArticleById)
                .collect(Collectors.toList());
    }

    public long getFavoriteCount(Long userId) {
        return userFavoriteRepository.countByUserId(userId);
    }
}