package com.blog.interfaces.controller;

import com.blog.application.dto.TagDTO;
import com.blog.application.service.TagService;
import com.blog.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Portal Tag Controller
 */
@RestController
@RequestMapping("/api/portal/tags")
@RequiredArgsConstructor
public class PortalTagController {

    private final TagService tagService;

    @GetMapping
    public Result<List<TagDTO>> getAllTags() {
        return Result.success(tagService.getAllTags());
    }

    @GetMapping("/popular")
    public Result<List<TagDTO>> getPopularTags(@RequestParam(defaultValue = "20") int limit) {
        return Result.success(tagService.getPopularTags(limit));
    }

    @GetMapping("/{id}")
    public Result<TagDTO> getTag(@PathVariable Long id) {
        return Result.success(tagService.getTagById(id));
    }
}