package com.blog.interfaces.controller;

import com.blog.application.dto.*;
import com.blog.application.service.InteractionService;
import com.blog.application.service.UserService;
import com.blog.common.Result;
import com.blog.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Portal User Controller
 */
@RestController
@RequestMapping("/api/portal/user")
@RequiredArgsConstructor
public class PortalUserController {

    private final UserService userService;
    private final InteractionService interactionService;

    @GetMapping("/me")
    public Result<UserDTO> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        return Result.success(userService.getCurrentUser(principal.getUserId()));
    }

    @PutMapping("/me")
    public Result<UserDTO> updateProfile(
            @RequestBody UserDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        return Result.success(userService.updateProfile(principal.getUserId(), dto));
    }

    @PostMapping("/articles/{articleId}/like")
    public Result<Boolean> toggleLike(
            @PathVariable Long articleId,
            @AuthenticationPrincipal UserPrincipal principal) {
        return Result.success(interactionService.toggleLike(articleId, principal.getUserId()));
    }

    @PostMapping("/articles/{articleId}/favorite")
    public Result<Boolean> toggleFavorite(
            @PathVariable Long articleId,
            @AuthenticationPrincipal UserPrincipal principal) {
        return Result.success(interactionService.toggleFavorite(articleId, principal.getUserId()));
    }

    @GetMapping("/favorites")
    public Result<List<ArticleDTO>> getUserFavorites(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal principal) {
        return Result.success(interactionService.getUserFavorites(principal.getUserId(), page, size));
    }

    @GetMapping("/likes")
    public Result<List<ArticleDTO>> getUserLikes(
            @AuthenticationPrincipal UserPrincipal principal) {
        return Result.success(interactionService.getUserLikes(principal.getUserId()));
    }
}