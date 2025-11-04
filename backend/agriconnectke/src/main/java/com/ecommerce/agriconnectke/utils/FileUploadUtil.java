package com.ecommerce.agriconnectke.utils;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileUploadUtil {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private final Tika tika = new Tika();

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_IMAGE_TYPES = {
        "image/jpeg", "image/png", "image/gif", "image/webp"
    };

    public String saveFile(MultipartFile file) throws IOException {
        validateFile(file);

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return "/uploads/" + uniqueFilename; // Return relative path for web access
    }

    public void deleteFile(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        // Extract filename from URL
        String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        Path filePath = Paths.get(uploadDir, filename);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    public boolean isImageFile(MultipartFile file) {
        try {
            String mimeType = tika.detect(file.getInputStream());
            for (String allowedType : ALLOWED_IMAGE_TYPES) {
                if (allowedType.equals(mimeType)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public void validateFileSize(MultipartFile file, long maxSize) {
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size: " + maxSize + " bytes");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }

        if (!isImageFile(file)) {
            throw new IllegalArgumentException("File must be an image (JPEG, PNG, GIF, WebP)");
        }

        validateFileSize(file, MAX_FILE_SIZE);
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return ".jpg"; // Default extension
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}