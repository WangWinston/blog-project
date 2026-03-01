package com.blog.application.service;

import com.blog.application.dto.TagDTO;
import com.blog.domain.model.Tag;
import com.blog.domain.repository.TagRepository;
import com.blog.common.exception.BlogException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Tag Application Service
 */
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public TagDTO createTag(TagDTO dto) {
        if (StringUtils.isBlank(dto.getName())) {
            throw new BlogException(400, "Tag name is required");
        }

        if (tagRepository.existsByName(dto.getName())) {
            throw new BlogException(400, "Tag name already exists");
        }

        Tag tag = new Tag();
        tag.setName(dto.getName());
        tag.setSlug(generateSlug(dto.getName()));
        tag.setDescription(dto.getDescription());
        tag.setColor(dto.getColor() != null ? dto.getColor() : "#3B82F6");
        tag.setArticleCount(0);

        Tag saved = tagRepository.save(tag);
        return toTagDTO(saved);
    }

    @Transactional
    public TagDTO updateTag(Long id, TagDTO dto) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new BlogException(404, "Tag not found"));

        tag.setName(dto.getName());
        tag.setDescription(dto.getDescription());
        tag.setColor(dto.getColor());

        Tag updated = tagRepository.update(tag);
        return toTagDTO(updated);
    }

    @Transactional
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    public TagDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new BlogException(404, "Tag not found"));
        return toTagDTO(tag);
    }

    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::toTagDTO)
                .collect(Collectors.toList());
    }

    public List<TagDTO> getPopularTags(int limit) {
        return tagRepository.findPopular(limit).stream()
                .map(this::toTagDTO)
                .collect(Collectors.toList());
    }

    private String generateSlug(String name) {
        if (StringUtils.isBlank(name)) {
            return "tag-" + System.currentTimeMillis();
        }
        String slug = name.toLowerCase()
                .replaceAll("[^a-z0-9\\u4e00-\\u9fa5]+", "-")
                .replaceAll("^-|-$", "");
        return StringUtils.isNotBlank(slug) ? slug : "tag-" + System.currentTimeMillis();
    }

    private TagDTO toTagDTO(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setSlug(tag.getSlug());
        dto.setDescription(tag.getDescription());
        dto.setColor(tag.getColor());
        dto.setArticleCount(tag.getArticleCount());
        return dto;
    }
}