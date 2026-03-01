package com.blog.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.domain.model.ArticleVersion;
import com.blog.domain.repository.ArticleVersionRepository;
import com.blog.infrastructure.persistence.mapper.ArticleVersionMapper;
import com.blog.infrastructure.persistence.po.ArticleVersionPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Article Version Repository Implementation
 */
@Repository
@RequiredArgsConstructor
public class ArticleVersionRepositoryImpl implements ArticleVersionRepository {

    private final ArticleVersionMapper articleVersionMapper;

    @Override
    public ArticleVersion save(ArticleVersion version) {
        ArticleVersionPO po = toArticleVersionPO(version);
        articleVersionMapper.insert(po);
        version.setId(po.getId());
        return version;
    }

    @Override
    public Optional<ArticleVersion> findById(Long id) {
        ArticleVersionPO po = articleVersionMapper.selectById(id);
        return po != null ? Optional.of(toArticleVersion(po)) : Optional.empty();
    }

    @Override
    public List<ArticleVersion> findByArticleId(Long articleId) {
        LambdaQueryWrapper<ArticleVersionPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleVersionPO::getArticleId, articleId)
                .orderByDesc(ArticleVersionPO::getVersion);
        return articleVersionMapper.selectList(wrapper).stream()
                .map(this::toArticleVersion)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ArticleVersion> findLatestByArticleId(Long articleId) {
        LambdaQueryWrapper<ArticleVersionPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleVersionPO::getArticleId, articleId)
                .orderByDesc(ArticleVersionPO::getVersion)
                .last("LIMIT 1");
        ArticleVersionPO po = articleVersionMapper.selectOne(wrapper);
        return po != null ? Optional.of(toArticleVersion(po)) : Optional.empty();
    }

    @Override
    public Optional<ArticleVersion> findByArticleIdAndVersion(Long articleId, Integer version) {
        LambdaQueryWrapper<ArticleVersionPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleVersionPO::getArticleId, articleId)
                .eq(ArticleVersionPO::getVersion, version);
        ArticleVersionPO po = articleVersionMapper.selectOne(wrapper);
        return po != null ? Optional.of(toArticleVersion(po)) : Optional.empty();
    }

    @Override
    public int countByArticleId(Long articleId) {
        LambdaQueryWrapper<ArticleVersionPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleVersionPO::getArticleId, articleId);
        return Math.toIntExact(articleVersionMapper.selectCount(wrapper));
    }

    @Override
    public void deleteById(Long id) {
        articleVersionMapper.deleteById(id);
    }

    private ArticleVersion toArticleVersion(ArticleVersionPO po) {
        ArticleVersion version = new ArticleVersion();
        version.setId(po.getId());
        version.setArticleId(po.getArticleId());
        version.setVersion(po.getVersion());
        version.setTitle(po.getTitle());
        version.setContent(po.getContent());
        version.setChangeLog(po.getChangeLog());
        version.setEditorId(po.getEditorId());
        version.setCreatedTime(po.getCreatedTime());
        return version;
    }

    private ArticleVersionPO toArticleVersionPO(ArticleVersion version) {
        ArticleVersionPO po = new ArticleVersionPO();
        po.setId(version.getId());
        po.setArticleId(version.getArticleId());
        po.setVersion(version.getVersion());
        po.setTitle(version.getTitle());
        po.setContent(version.getContent());
        po.setChangeLog(version.getChangeLog());
        po.setEditorId(version.getEditorId());
        return po;
    }
}