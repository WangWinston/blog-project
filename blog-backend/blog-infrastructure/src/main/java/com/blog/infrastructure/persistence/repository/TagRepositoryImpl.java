package com.blog.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.blog.domain.model.Tag;
import com.blog.domain.repository.TagRepository;
import com.blog.infrastructure.persistence.mapper.ArticleTagMapper;
import com.blog.infrastructure.persistence.mapper.TagMapper;
import com.blog.infrastructure.persistence.po.ArticleTagPO;
import com.blog.infrastructure.persistence.po.TagPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Tag Repository Implementation
 */
@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final TagMapper tagMapper;
    private final ArticleTagMapper articleTagMapper;

    @Override
    public Tag save(Tag tag) {
        TagPO po = toTagPO(tag);
        tagMapper.insert(po);
        tag.setId(po.getId());
        return tag;
    }

    @Override
    public Tag update(Tag tag) {
        TagPO po = toTagPO(tag);
        tagMapper.updateById(po);
        return tag;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        TagPO po = tagMapper.selectById(id);
        return po != null ? Optional.of(toTag(po)) : Optional.empty();
    }

    @Override
    public Optional<Tag> findBySlug(String slug) {
        LambdaQueryWrapper<TagPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TagPO::getSlug, slug);
        TagPO po = tagMapper.selectOne(wrapper);
        return po != null ? Optional.of(toTag(po)) : Optional.empty();
    }

    @Override
    public List<Tag> findAll() {
        return tagMapper.selectList(null).stream()
                .map(this::toTag)
                .collect(Collectors.toList());
    }

    @Override
    public List<Tag> findByArticleId(Long articleId) {
        LambdaQueryWrapper<ArticleTagPO> atWrapper = new LambdaQueryWrapper<>();
        atWrapper.eq(ArticleTagPO::getArticleId, articleId);
        List<ArticleTagPO> articleTags = articleTagMapper.selectList(atWrapper);

        if (articleTags.isEmpty()) {
            return List.of();
        }

        List<Long> tagIds = articleTags.stream()
                .map(ArticleTagPO::getTagId)
                .collect(Collectors.toList());

        return tagMapper.selectBatchIds(tagIds).stream()
                .map(this::toTag)
                .collect(Collectors.toList());
    }

    @Override
    public List<Tag> findPopular(int limit) {
        LambdaQueryWrapper<TagPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(TagPO::getArticleCount)
                .last("LIMIT " + limit);
        return tagMapper.selectList(wrapper).stream()
                .map(this::toTag)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        LambdaQueryWrapper<TagPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TagPO::getName, name);
        return tagMapper.selectCount(wrapper) > 0;
    }

    @Override
    public void deleteById(Long id) {
        // Delete article-tag relations first
        LambdaQueryWrapper<ArticleTagPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTagPO::getTagId, id);
        articleTagMapper.delete(wrapper);

        tagMapper.deleteById(id);
    }

    @Override
    public void updateArticleCount(Long id, int delta) {
        LambdaUpdateWrapper<TagPO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TagPO::getId, id)
                .setSql("article_count = article_count + " + delta);
        tagMapper.update(null, wrapper);
    }

    @Override
    public void addToArticle(Long articleId, Long tagId) {
        LambdaQueryWrapper<ArticleTagPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTagPO::getArticleId, articleId)
                .eq(ArticleTagPO::getTagId, tagId);
        if (articleTagMapper.selectCount(wrapper) == 0) {
            ArticleTagPO po = new ArticleTagPO();
            po.setArticleId(articleId);
            po.setTagId(tagId);
            articleTagMapper.insert(po);
        }
    }

    @Override
    public void removeFromArticle(Long articleId, Long tagId) {
        LambdaQueryWrapper<ArticleTagPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTagPO::getArticleId, articleId)
                .eq(ArticleTagPO::getTagId, tagId);
        articleTagMapper.delete(wrapper);
    }

    @Override
    public void removeAllFromArticle(Long articleId) {
        LambdaQueryWrapper<ArticleTagPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTagPO::getArticleId, articleId);
        articleTagMapper.delete(wrapper);
    }

    private Tag toTag(TagPO po) {
        Tag tag = new Tag();
        tag.setId(po.getId());
        tag.setName(po.getName());
        tag.setSlug(po.getSlug());
        tag.setDescription(po.getDescription());
        tag.setColor(po.getColor());
        tag.setArticleCount(po.getArticleCount());
        tag.setCreatedTime(po.getCreatedTime());
        tag.setUpdatedTime(po.getUpdatedTime());
        return tag;
    }

    private TagPO toTagPO(Tag tag) {
        TagPO po = new TagPO();
        po.setId(tag.getId());
        po.setName(tag.getName());
        po.setSlug(tag.getSlug());
        po.setDescription(tag.getDescription());
        po.setColor(tag.getColor());
        po.setArticleCount(tag.getArticleCount());
        return po;
    }
}