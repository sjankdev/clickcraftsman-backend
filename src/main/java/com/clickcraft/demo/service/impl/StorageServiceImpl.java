package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class StorageServiceImpl implements StorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public byte[] storeFile(MultipartFile file, Long freelancerId) throws IOException {
        Path freelancerDir = Paths.get(uploadDir + "/" + freelancerId);
        if (!Files.exists(freelancerDir)) {
            Files.createDirectories(freelancerDir);
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = freelancerDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return Files.readAllBytes(filePath);
    }
}
