package com.blog.interfaces.controller;

import com.blog.application.dto.TagDTO;
import com.blog.application.service.TagService;
import com.blog.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin Tag Controller
 */
@RestController
@RequestMapping("/api/admin/tags")
@RequiredArgsConstructor
public class AdminTagController {

    private final TagService tagService;

    @GetMapping
    public Result<List<TagDTO>> getAllTags() {
        return Result.success(tagService.getAllTags());
    }

    @GetMapping("/{id}")
    public Result<TagDTO> getTag(@PathVariable Long id) {
        return Result.success(tagService.getTagById(id));
    }

    @PostMapping
    public Result<TagDTO> createTag(@RequestBody TagDTO dto) {
        return Result.success(tagService.createTag(dto));
    }

    @PutMapping("/{id}")
    public Result<TagDTO> updateTag(@PathVariable Long id, @RequestBody TagDTO dto) {
        return Result.success(tagService.updateTag(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success();
    }
}