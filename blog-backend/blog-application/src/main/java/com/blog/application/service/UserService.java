package com.blog.application.service;

import com.blog.application.dto.*;
import com.blog.common.exception.BlogException;
import com.blog.domain.model.Profile;
import com.blog.domain.model.User;
import com.blog.domain.repository.UserRepository;
import com.blog.infrastructure.external.GitHubOAuthClient;
import com.blog.infrastructure.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User Application Service
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public UserDTO register(RegisterRequest request) {
        // Validate
        if (StringUtils.isBlank(request.getUsername()) || StringUtils.isBlank(request.getPassword())) {
            throw new BlogException(400, "Username and password are required");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BlogException(400, "Username already exists");
        }

        if (StringUtils.isNotBlank(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BlogException(400, "Email already exists");
        }

        // Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole("USER");
        user.setStatus(1);

        User saved = userRepository.save(user);
        return toUserDTO(saved);
    }

    public LoginResponse login(LoginRequest request) {
        // Validate captcha token (simple validation - check format and timestamp)
        if (!validateCaptchaToken(request.getCaptchaToken())) {
            throw new BlogException(400, "Invalid captcha");
        }

        // Find user
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BlogException(401, "Invalid username or password"));

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BlogException(401, "Invalid username or password");
        }

        // Check status
        if (!user.isEnabled()) {
            throw new BlogException(403, "Account is disabled");
        }

        // Generate tokens
        String accessToken = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtUtils.generateRefreshToken(user.getId());

        return LoginResponse.of(accessToken, refreshToken, 7200L, toUserDTO(user));
    }

    @Transactional
    public LoginResponse githubLogin(String code, GitHubOAuthClient gitHubOAuthClient) {
        // Get access token
        String accessToken = gitHubOAuthClient.getAccessToken(code);
        if (StringUtils.isBlank(accessToken)) {
            throw new BlogException(401, "Failed to get GitHub access token");
        }

        // Get user info
        GitHubOAuthClient.GitHubUserInfo gitHubUser = gitHubOAuthClient.getUserInfo(accessToken);
        if (gitHubUser == null) {
            throw new BlogException(401, "Failed to get GitHub user info");
        }

        // Find or create user
        User user = userRepository.findByGithubId(gitHubUser.getId())
                .orElseGet(() -> createGitHubUser(gitHubUser));

        // Generate tokens
        String jwtAccessToken = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtUtils.generateRefreshToken(user.getId());

        return LoginResponse.of(jwtAccessToken, refreshToken, 7200L, toUserDTO(user));
    }

    private User createGitHubUser(GitHubOAuthClient.GitHubUserInfo gitHubUser) {
        User user = new User();
        user.setUsername("github_" + gitHubUser.getId());
        user.setEmail(gitHubUser.getEmail());
        user.setAvatar(gitHubUser.getAvatarUrl());
        user.setGithubId(gitHubUser.getId());
        user.setRole("USER");
        user.setStatus(1);

        Profile profile = new Profile();
        profile.setNickname(gitHubUser.getName());
        profile.setBio(gitHubUser.getBio());
        profile.setWebsite(gitHubUser.getBlog());
        profile.setLocation(gitHubUser.getLocation());
        profile.setGithubUsername(gitHubUser.getLogin());
        user.setProfile(profile);

        return userRepository.save(user);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BlogException(404, "User not found"));
        return toUserDTO(user);
    }

    public UserDTO getCurrentUser(Long userId) {
        return getUserById(userId);
    }

    @Transactional
    public UserDTO updateProfile(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BlogException(404, "User not found"));

        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
        }
        profile.setNickname(userDTO.getNickname());
        profile.setBio(userDTO.getBio());
        profile.setWebsite(userDTO.getWebsite());
        profile.setLocation(userDTO.getLocation());
        user.setProfile(profile);

        if (StringUtils.isNotBlank(userDTO.getAvatar())) {
            user.setAvatar(userDTO.getAvatar());
        }

        User updated = userRepository.update(user);
        return toUserDTO(updated);
    }

    private boolean validateCaptchaToken(String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        try {
            // Simple validation: Base64 decode and check timestamp
            String decoded = new String(java.util.Base64.getDecoder().decode(token));
            String[] parts = decoded.split("\\|");
            if (parts.length < 2) {
                return false;
            }
            long timestamp = Long.parseLong(parts[0]);
            // Token valid for 5 minutes
            return System.currentTimeMillis() - timestamp < 5 * 60 * 1000;
        } catch (Exception e) {
            return false;
        }
    }

    private UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAvatar(user.getAvatar());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setGithubId(user.getGithubId());
        dto.setCreatedAt(user.getCreatedTime());

        if (user.getProfile() != null) {
            Profile profile = user.getProfile();
            dto.setNickname(profile.getNickname());
            dto.setBio(profile.getBio());
            dto.setWebsite(profile.getWebsite());
            dto.setLocation(profile.getLocation());
        }

        return dto;
    }
}