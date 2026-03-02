package com.blog.application.service;

import com.blog.common.exception.BlogException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * File Upload Service
 */
@Slf4j
@Service
public class FileService {

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @Value("${file.upload.max-size:10485760}")
    private long maxFileSize;

    @Value("${file.upload.allowed-types:image/jpeg,image/png,image/gif,image/webp,application/pdf}")
    private String allowedTypes;

    /**
     * Upload single file
     */
    public String uploadFile(MultipartFile file, String type) {
        validateFile(file);

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String newFilename = generateFilename(extension);

            Path uploadDir = getUploadDir(type);
            Path filePath = uploadDir.resolve(newFilename);

            Files.createDirectories(uploadDir);
            Files.copy(file.getInputStream(), filePath);

            log.info("File uploaded successfully: {}", filePath);
            return getAccessibleUrl(type, newFilename);

        } catch (IOException e) {
            log.error("Failed to upload file", e);
            throw new BlogException(500, "Failed to upload file: " + e.getMessage());
        }
    }

    /**
     * Upload multiple files
     */
    public List<String> uploadFiles(MultipartFile[] files, String type) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                urls.add(uploadFile(file, type));
            }
        }
        return urls;
    }

    /**
     * Delete file
     */
    public void deleteFile(String url) {
        try {
            String filename = extractFilenameFromUrl(url);
            Path filePath = Paths.get(uploadPath, filename);
            Files.deleteIfExists(filePath);
            log.info("File deleted successfully: {}", filePath);
        } catch (IOException e) {
            log.error("Failed to delete file", e);
            throw new BlogException(500, "Failed to delete file");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BlogException(400, "File cannot be empty");
        }

        if (file.getSize() > maxFileSize) {
            throw new BlogException(400, "File size exceeds the limit: " + maxFileSize + " bytes");
        }

        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new BlogException(400, "File type not allowed: " + contentType);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }

    private String generateFilename(String extension) {
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }

    private Path getUploadDir(String type) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return Paths.get(uploadPath, type, datePath);
    }

    private String getAccessibleUrl(String type, String filename) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return "/uploads/" + type + "/" + datePath + "/" + filename;
    }

    private String extractFilenameFromUrl(String url) {
        if (url == null) {
            return "";
        }
        return url.replace("/uploads/", "");
    }
}