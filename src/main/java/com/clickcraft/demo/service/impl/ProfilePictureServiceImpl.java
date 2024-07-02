package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.service.ProfilePictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ProfilePictureServiceImpl implements ProfilePictureService {

    @Value("classpath:${default.profile.picture.path}")
    private Resource defaultImageResource;

    public byte[] getDefaultProfilePicture() {
        try (InputStream inputStream = defaultImageResource.getInputStream()) {
            return StreamUtils.copyToByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error reading default profile picture: " + e.getMessage(), e);
        }
    }
}