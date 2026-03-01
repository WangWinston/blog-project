package com.blog.application.service;

import com.blog.application.dto.ArticleDTO;
import com.blog.application.dto.CommentDTO;
import com.blog.application.dto.DashboardStatsDTO;
import com.blog.domain.repository.ArticleRepository;
import com.blog.domain.repository.CommentRepository;
import com.blog.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Dashboard Application Service
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final CommentService commentService;

    public DashboardStatsDTO getStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        long totalArticles = articleRepository.countPublished();
        stats.setTotalArticles(totalArticles);
        stats.setPublishedArticles(totalArticles);

        long totalUsers = userRepository.count();
        stats.setTotalUsers(totalUsers);

        long totalComments = commentRepository.countByArticleId(null);
        long pendingComments = commentRepository.countPending();
        stats.setTotalComments(totalComments);
        stats.setPendingComments(pendingComments);

        // These would be calculated from analytics in a real application
        stats.setTotalViews(0L);
        stats.setTotalLikes(0L);
        stats.setTodayViews(0L);
        stats.setTodayComments(0L);
        stats.setDraftArticles(0L);

        return stats;
    }

    public List<ArticleDTO> getHotArticles(int limit) {
        return articleService.getHotArticles(limit);
    }

    public List<CommentDTO> getRecentComments(int limit) {
        return commentService.getRecentComments(limit);
    }
}