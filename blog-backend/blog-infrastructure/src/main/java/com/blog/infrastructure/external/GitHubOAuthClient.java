package com.blog.infrastructure.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * GitHub OAuth Client
 */
@Slf4j
@Component
public class GitHubOAuthClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${github.oauth.client-id:}")
    private String clientId;

    @Value("${github.oauth.client-secret:}")
    private String clientSecret;

    @Value("${github.oauth.redirect-uri:}")
    private String redirectUri;

    private static final String GITHUB_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String GITHUB_USER_API_URL = "https://api.github.com/user";

    public GitHubOAuthClient() {
        this.webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Get access token from GitHub
     */
    public String getAccessToken(String code) {
        try {
            String response = webClient.post()
                    .uri(GITHUB_ACCESS_TOKEN_URL)
                    .header("Accept", "application/json")
                    .bodyValue(Map.of(
                            "client_id", clientId,
                            "client_secret", clientSecret,
                            "code", code,
                            "redirect_uri", redirectUri
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            @SuppressWarnings("unchecked")
            Map<String, Object> result = objectMapper.readValue(response, Map.class);
            return (String) result.get("access_token");
        } catch (Exception e) {
            log.error("Failed to get GitHub access token", e);
            return null;
        }
    }

    /**
     * Get GitHub user info
     */
    public GitHubUserInfo getUserInfo(String accessToken) {
        try {
            String response = webClient.get()
                    .uri(GITHUB_USER_API_URL)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", "application/json")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            @SuppressWarnings("unchecked")
            Map<String, Object> userInfo = objectMapper.readValue(response, Map.class);

            GitHubUserInfo info = new GitHubUserInfo();
            info.setId(String.valueOf(userInfo.get("id")));
            info.setLogin((String) userInfo.get("login"));
            info.setName((String) userInfo.get("name"));
            info.setEmail((String) userInfo.get("email"));
            info.setAvatarUrl((String) userInfo.get("avatar_url"));
            info.setBio((String) userInfo.get("bio"));
            info.setBlog((String) userInfo.get("blog"));
            info.setLocation((String) userInfo.get("location"));

            return info;
        } catch (Exception e) {
            log.error("Failed to get GitHub user info", e);
            return null;
        }
    }

    /**
     * GitHub User Info DTO
     */
    public static class GitHubUserInfo {
        private String id;
        private String login;
        private String name;
        private String email;
        private String avatarUrl;
        private String bio;
        private String blog;
        private String location;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getBlog() {
            return blog;
        }

        public void setBlog(String blog) {
            this.blog = blog;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}