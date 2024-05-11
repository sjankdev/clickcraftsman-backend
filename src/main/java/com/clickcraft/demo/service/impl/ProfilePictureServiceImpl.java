package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.service.ProfilePictureService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ProfilePictureServiceImpl implements ProfilePictureService {

    public byte[] getDefaultProfilePicture() {
        String defaultImagePath = "/default-profile-pictures/default-profile-image.jpg";

        // Load the default image using the class loader
        try (InputStream inputStream = getClass().getResourceAsStream(defaultImagePath)) {
            if (inputStream != null) {
                byte[] imageData = inputStream.readAllBytes();
                if (imageData.length == 0) {
                    System.err.println("Warning: Default profile picture file is empty: " + defaultImagePath);
                }
                return imageData;
            } else {
                System.err.println("Error: Default profile picture not found: " + defaultImagePath);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error reading default profile picture: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
}
