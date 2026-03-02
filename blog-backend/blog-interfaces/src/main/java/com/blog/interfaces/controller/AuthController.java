package com.blog.interfaces.controller;

import com.blog.application.dto.*;
import com.blog.application.service.UserService;
import com.blog.common.Result;
import com.blog.infrastructure.external.GitHubOAuthClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Auth Controller
 */
@Tag(name = "Authentication", description = "User authentication API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final GitHubOAuthClient gitHubOAuthClient;

    @Value("${github.oauth.client-id:}")
    private String githubClientId;

    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(userService.login(request));
    }

    @Operation(summary = "User registration", description = "Register a new user account")
    @PostMapping("/register")
    public Result<UserDTO> register(@Valid @RequestBody RegisterRequest request) {
        return Result.success(userService.register(request));
    }

    @Operation(summary = "GitHub OAuth login", description = "Redirect to GitHub OAuth authorization page")
    @GetMapping("/github")
    public void githubLogin(HttpServletResponse response) throws IOException {
        String redirectUrl = "https://github.com/login/oauth/authorize?client_id=" + githubClientId +
                "&scope=user:email";
        response.sendRedirect(redirectUrl);
    }

    @Operation(summary = "GitHub OAuth callback", description = "Handle GitHub OAuth callback and authenticate user")
    @GetMapping("/github/callback")
    public Result<LoginResponse> githubCallback(@RequestParam String code) {
        return Result.success(userService.githubLogin(code, gitHubOAuthClient));
    }

    @Operation(summary = "User logout", description = "Logout user (client should discard token)")
    @PostMapping("/logout")
    public Result<Void> logout() {
        // JWT is stateless, just return success
        return Result.success();
    }
}