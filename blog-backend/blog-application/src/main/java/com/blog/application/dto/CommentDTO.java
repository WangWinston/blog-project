package com.blog.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Comment DTO
 */
@Data
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long articleId;
    private String articleTitle;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Long parentId;
    private Long rootId;
    private Long replyToUserId;
    private String replyToUserName;
    private String content;
    private String status;
    private Integer likeCount;
    private LocalDateTime createdTime;
    private List<CommentDTO> children;
}