package com.clickcraft.demo.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface StorageService {

     byte[] storeFile(MultipartFile file, Long freelancerId) throws IOException;

    }
