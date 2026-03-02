package com.blog.interfaces.controller;

import com.blog.application.service.FileService;
import com.blog.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * File Upload Controller
 */
@Tag(name = "File Upload", description = "File upload API")
@RestController
@RequestMapping("/api/admin/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileService fileService;

    @Operation(summary = "Upload single file")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> uploadFile(
            @Parameter(description = "File to upload") @RequestParam("file") MultipartFile file,
            @Parameter(description = "File type (image, document, etc.)") @RequestParam(defaultValue = "image") String type) {
        String url = fileService.uploadFile(file, type);
        return Result.success(url);
    }

    @Operation(summary = "Upload multiple files")
    @PostMapping(value = "/upload-batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<List<String>> uploadFiles(
            @Parameter(description = "Files to upload") @RequestParam("files") MultipartFile[] files,
            @Parameter(description = "File type") @RequestParam(defaultValue = "image") String type) {
        List<String> urls = fileService.uploadFiles(files, type);
        return Result.success(urls);
    }

    @Operation(summary = "Delete file")
    @DeleteMapping
    public Result<Void> deleteFile(@Parameter(description = "File URL") @RequestParam String url) {
        fileService.deleteFile(url);
        return Result.success();
    }
}