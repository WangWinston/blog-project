package com.blog.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Article Persistence Object
 */
@Data
@TableName("article")
public class ArticlePO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String slug;

    private String summary;

    private String content;

    private String coverImage;

    private Long categoryId;

    private Long authorId;

    private String status;

    private Integer isTop;

    private Integer isRecommend;

    private Integer viewCount;

    private Integer likeCount;

    private Integer commentCount;

    private LocalDateTime scheduledPublishTime;

    private LocalDateTime publishedTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @TableLogic
    private Integer deleted;
}