package com.blog.interfaces.controller;

import com.blog.application.dto.CommentDTO;
import com.blog.application.service.CommentService;
import com.blog.common.Result;
import com.blog.infrastructure.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Portal Comment Controller
 */
@RestController
@RequestMapping("/api/portal/comments")
@RequiredArgsConstructor
public class PortalCommentController {

    private final CommentService commentService;

    @GetMapping("/article/{articleId}")
    public Result<List<CommentDTO>> getArticleComments(@PathVariable Long articleId) {
        return Result.success(commentService.getArticleComments(articleId));
    }

    @PostMapping
    public Result<CommentDTO> createComment(
            @RequestBody CommentDTO dto,
            @AuthenticationPrincipal UserPrincipal principal,
            HttpServletRequest request) {

        CommentDTO comment = commentService.createComment(
                dto.getArticleId(),
                principal.getUserId(),
                dto.getContent(),
                dto.getParentId(),
                dto.getReplyToUserId()
        );

        return Result.success(comment);
    }

    @GetMapping("/recent")
    public Result<List<CommentDTO>> getRecentComments(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(commentService.getRecentComments(limit));
    }
}