package com.blog.interfaces.controller;

import com.blog.application.dto.CommentDTO;
import com.blog.application.service.CommentService;
import com.blog.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin Comment Controller
 */
@RestController
@RequestMapping("/api/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService commentService;

    @GetMapping
    public Result<List<CommentDTO>> getPendingComments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.success(commentService.getPendingComments(page, size));
    }

    @PostMapping("/{id}/approve")
    public Result<Void> approveComment(@PathVariable Long id) {
        commentService.approveComment(id);
        return Result.success();
    }

    @PostMapping("/{id}/reject")
    public Result<Void> rejectComment(@PathVariable Long id) {
        commentService.rejectComment(id);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return Result.success();
    }
}