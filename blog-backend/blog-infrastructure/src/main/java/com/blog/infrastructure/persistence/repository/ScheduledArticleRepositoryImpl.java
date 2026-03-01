package com.blog.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.blog.domain.model.ScheduledArticle;
import com.blog.domain.repository.ScheduledArticleRepository;
import com.blog.infrastructure.persistence.mapper.ScheduledArticleMapper;
import com.blog.infrastructure.persistence.po.ScheduledArticlePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Scheduled Article Repository Implementation
 */
@Repository
@RequiredArgsConstructor
public class ScheduledArticleRepositoryImpl implements ScheduledArticleRepository {

    private final ScheduledArticleMapper scheduledArticleMapper;

    @Override
    public ScheduledArticle save(ScheduledArticle scheduledArticle) {
        ScheduledArticlePO po = toScheduledArticlePO(scheduledArticle);
        scheduledArticleMapper.insert(po);
        scheduledArticle.setId(po.getId());
        return scheduledArticle;
    }

    @Override
    public Optional<ScheduledArticle> findById(Long id) {
        ScheduledArticlePO po = scheduledArticleMapper.selectById(id);
        return po != null ? Optional.of(toScheduledArticle(po)) : Optional.empty();
    }

    @Override
    public Optional<ScheduledArticle> findByArticleId(Long articleId) {
        LambdaQueryWrapper<ScheduledArticlePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduledArticlePO::getArticleId, articleId)
                .eq(ScheduledArticlePO::getStatus, "PENDING");
        ScheduledArticlePO po = scheduledArticleMapper.selectOne(wrapper);
        return po != null ? Optional.of(toScheduledArticle(po)) : Optional.empty();
    }

    @Override
    public List<ScheduledArticle> findPendingBeforeTime(LocalDateTime time) {
        LambdaQueryWrapper<ScheduledArticlePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduledArticlePO::getStatus, "PENDING")
                .le(ScheduledArticlePO::getScheduledTime, time);
        return scheduledArticleMapper.selectList(wrapper).stream()
                .map(this::toScheduledArticle)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduledArticle> findAllPending() {
        LambdaQueryWrapper<ScheduledArticlePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduledArticlePO::getStatus, "PENDING")
                .orderByAsc(ScheduledArticlePO::getScheduledTime);
        return scheduledArticleMapper.selectList(wrapper).stream()
                .map(this::toScheduledArticle)
                .collect(Collectors.toList());
    }

    @Override
    public void updateStatus(Long id, String status, String errorMessage) {
        LambdaUpdateWrapper<ScheduledArticlePO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ScheduledArticlePO::getId, id)
                .set(ScheduledArticlePO::getStatus, status)
                .set(ScheduledArticlePO::getErrorMessage, errorMessage);
        scheduledArticleMapper.update(null, wrapper);
    }

    @Override
    public void deleteByArticleId(Long articleId) {
        LambdaQueryWrapper<ScheduledArticlePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduledArticlePO::getArticleId, articleId);
        scheduledArticleMapper.delete(wrapper);
    }

    private ScheduledArticle toScheduledArticle(ScheduledArticlePO po) {
        ScheduledArticle scheduledArticle = new ScheduledArticle();
        scheduledArticle.setId(po.getId());
        scheduledArticle.setArticleId(po.getArticleId());
        scheduledArticle.setScheduledTime(po.getScheduledTime());
        scheduledArticle.setStatus(po.getStatus());
        scheduledArticle.setErrorMessage(po.getErrorMessage());
        scheduledArticle.setCreatedTime(po.getCreatedTime());
        scheduledArticle.setUpdatedTime(po.getUpdatedTime());
        return scheduledArticle;
    }

    private ScheduledArticlePO toScheduledArticlePO(ScheduledArticle scheduledArticle) {
        ScheduledArticlePO po = new ScheduledArticlePO();
        po.setId(scheduledArticle.getId());
        po.setArticleId(scheduledArticle.getArticleId());
        po.setScheduledTime(scheduledArticle.getScheduledTime());
        po.setStatus(scheduledArticle.getStatus());
        po.setErrorMessage(scheduledArticle.getErrorMessage());
        return po;
    }
}