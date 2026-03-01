package com.blog.application.service;

import com.blog.application.dto.CommentDTO;
import com.blog.domain.model.Comment;
import com.blog.domain.repository.ArticleRepository;
import com.blog.domain.repository.CommentRepository;
import com.blog.domain.repository.UserRepository;
import com.blog.domain.service.CommentDomainService;
import com.blog.common.exception.BlogException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Comment Application Service
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentDomainService commentDomainService;

    @Transactional
    public CommentDTO createComment(Long articleId, Long userId, String content, Long parentId, Long replyToUserId) {
        // Validate article exists
        articleRepository.findById(articleId)
                .orElseThrow(() -> new BlogException(404, "Article not found"));

        // Validate content
        if (!commentDomainService.isValidContent(content)) {
            throw new BlogException(400, "Invalid comment content");
        }

        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentId(parentId);
        comment.setReplyToUserId(replyToUserId);
        comment.setStatus("APPROVED"); // Auto-approve for now, can change to PENDING for moderation
        comment.setLikeCount(0);

        // Set root ID for nested replies
        if (parentId != null && parentId != 0) {
            Comment parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new BlogException(404, "Parent comment not found"));
            comment.setRootId(parent.getRootId() != null ? parent.getRootId() : parentId);
        }

        Comment saved = commentRepository.save(comment);
        return toCommentDTO(saved);
    }

    @Transactional
    public void approveComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BlogException(404, "Comment not found"));

        comment.setStatus("APPROVED");
        commentRepository.update(comment);
    }

    @Transactional
    public void rejectComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BlogException(404, "Comment not found"));

        comment.setStatus("SPAM");
        commentRepository.update(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public List<CommentDTO> getArticleComments(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        List<Comment> tree = commentDomainService.buildTree(comments);
        return tree.stream()
                .map(this::toCommentDTO)
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getPendingComments(int page, int size) {
        List<Comment> comments = commentRepository.findPending(page, size);
        return comments.stream()
                .map(this::toCommentDTO)
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getRecentComments(int limit) {
        List<Comment> comments = commentRepository.findRecent(limit);
        return comments.stream()
                .map(this::toCommentDTO)
                .collect(Collectors.toList());
    }

    private CommentDTO toCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setArticleId(comment.getArticleId());
        dto.setUserId(comment.getUserId());
        dto.setParentId(comment.getParentId());
        dto.setRootId(comment.getRootId());
        dto.setReplyToUserId(comment.getReplyToUserId());
        dto.setContent(comment.getContent());
        dto.setStatus(comment.getStatus());
        dto.setLikeCount(comment.getLikeCount());
        dto.setCreatedTime(comment.getCreatedTime());

        // Load user info
        userRepository.findById(comment.getUserId())
                .ifPresent(user -> {
                    dto.setUserName(user.getUsername());
                    dto.setUserAvatar(user.getAvatar());
                });

        // Load reply to user name
        if (comment.getReplyToUserId() != null) {
            userRepository.findById(comment.getReplyToUserId())
                    .ifPresent(user -> dto.setReplyToUserName(user.getUsername()));
        }

        // Load children
        if (comment.getChildren() != null && !comment.getChildren().isEmpty()) {
            dto.setChildren(comment.getChildren().stream()
                    .map(this::toCommentDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}