package com.blog.interfaces.controller;

import com.blog.application.dto.CategoryDTO;
import com.blog.application.service.CategoryService;
import com.blog.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Portal Category Controller
 */
@RestController
@RequestMapping("/api/portal/categories")
@RequiredArgsConstructor
public class PortalCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Result<List<CategoryDTO>> getAllCategories() {
        return Result.success(categoryService.getAllCategories());
    }

    @GetMapping("/tree")
    public Result<List<CategoryDTO>> getCategoryTree() {
        return Result.success(categoryService.getCategoryTree());
    }

    @GetMapping("/{id}")
    public Result<CategoryDTO> getCategory(@PathVariable Long id) {
        return Result.success(categoryService.getCategoryById(id));
    }
}