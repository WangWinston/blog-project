package com.blog.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.domain.model.Comment;
import com.blog.domain.repository.CommentRepository;
import com.blog.infrastructure.persistence.mapper.CommentMapper;
import com.blog.infrastructure.persistence.po.CommentPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Comment Repository Implementation
 */
@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentMapper commentMapper;

    @Override
    public Comment save(Comment comment) {
        CommentPO po = toCommentPO(comment);
        commentMapper.insert(po);
        comment.setId(po.getId());
        return comment;
    }

    @Override
    public Comment update(Comment comment) {
        CommentPO po = toCommentPO(comment);
        commentMapper.updateById(po);
        return comment;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        CommentPO po = commentMapper.selectById(id);
        return po != null ? Optional.of(toComment(po)) : Optional.empty();
    }

    @Override
    public List<Comment> findByArticleId(Long articleId) {
        LambdaQueryWrapper<CommentPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommentPO::getArticleId, articleId)
                .eq(CommentPO::getStatus, "APPROVED")
                .orderByAsc(CommentPO::getCreatedTime);
        return commentMapper.selectList(wrapper).stream()
                .map(this::toComment)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> findByUserId(Long userId, int page, int size) {
        LambdaQueryWrapper<CommentPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommentPO::getUserId, userId)
                .orderByDesc(CommentPO::getCreatedTime);
        Page<CommentPO> pageResult = commentMapper.selectPage(new Page<>(page, size), wrapper);
        return pageResult.getRecords().stream()
                .map(this::toComment)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> findPending(int page, int size) {
        LambdaQueryWrapper<CommentPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommentPO::getStatus, "PENDING")
                .orderByDesc(CommentPO::getCreatedTime);
        Page<CommentPO> pageResult = commentMapper.selectPage(new Page<>(page, size), wrapper);
        return pageResult.getRecords().stream()
                .map(this::toComment)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> findRecent(int limit) {
        LambdaQueryWrapper<CommentPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommentPO::getStatus, "APPROVED")
                .orderByDesc(CommentPO::getCreatedTime)
                .last("LIMIT " + limit);
        return commentMapper.selectList(wrapper).stream()
                .map(this::toComment)
                .collect(Collectors.toList());
    }

    @Override
    public long countByArticleId(Long articleId) {
        LambdaQueryWrapper<CommentPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommentPO::getArticleId, articleId)
                .eq(CommentPO::getStatus, "APPROVED");
        return commentMapper.selectCount(wrapper);
    }

    @Override
    public long countPending() {
        LambdaQueryWrapper<CommentPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommentPO::getStatus, "PENDING");
        return commentMapper.selectCount(wrapper);
    }

    @Override
    public void deleteById(Long id) {
        commentMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, String status) {
        LambdaUpdateWrapper<CommentPO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CommentPO::getId, id)
                .set(CommentPO::getStatus, status);
        commentMapper.update(null, wrapper);
    }

    @Override
    public void updateArticleCommentCount(Long articleId) {
        // This would typically be handled by the article repository
        // but we count here for reference
    }

    private Comment toComment(CommentPO po) {
        Comment comment = new Comment();
        comment.setId(po.getId());
        comment.setArticleId(po.getArticleId());
        comment.setUserId(po.getUserId());
        comment.setParentId(po.getParentId());
        comment.setRootId(po.getRootId());
        comment.setReplyToUserId(po.getReplyToUserId());
        comment.setContent(po.getContent());
        comment.setStatus(po.getStatus());
        comment.setLikeCount(po.getLikeCount());
        comment.setIpAddress(po.getIpAddress());
        comment.setUserAgent(po.getUserAgent());
        comment.setCreatedTime(po.getCreatedTime());
        comment.setUpdatedTime(po.getUpdatedTime());
        return comment;
    }

    private CommentPO toCommentPO(Comment comment) {
        CommentPO po = new CommentPO();
        po.setId(comment.getId());
        po.setArticleId(comment.getArticleId());
        po.setUserId(comment.getUserId());
        po.setParentId(comment.getParentId());
        po.setRootId(comment.getRootId());
        po.setReplyToUserId(comment.getReplyToUserId());
        po.setContent(comment.getContent());
        po.setStatus(comment.getStatus());
        po.setLikeCount(comment.getLikeCount());
        po.setIpAddress(comment.getIpAddress());
        po.setUserAgent(comment.getUserAgent());
        return po;
    }
}