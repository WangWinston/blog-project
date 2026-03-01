package com.blog.interfaces.controller;

import com.blog.application.dto.*;
import com.blog.application.service.UserService;
import com.blog.common.Result;
import com.blog.infrastructure.external.GitHubOAuthClient;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Auth Controller
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final GitHubOAuthClient gitHubOAuthClient;

    @Value("${github.oauth.client-id:}")
    private String githubClientId;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        return Result.success(userService.login(request));
    }

    @PostMapping("/register")
    public Result<UserDTO> register(@RequestBody RegisterRequest request) {
        return Result.success(userService.register(request));
    }

    @GetMapping("/github")
    public void githubLogin(HttpServletResponse response) throws IOException {
        String redirectUrl = "https://github.com/login/oauth/authorize?client_id=" + githubClientId +
                "&scope=user:email";
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/github/callback")
    public Result<LoginResponse> githubCallback(@RequestParam String code) {
        return Result.success(userService.githubLogin(code, gitHubOAuthClient));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        // JWT is stateless, just return success
        return Result.success();
    }
}