package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.freelancer.FreelancerProfileDTO;
import com.clickcraft.demo.dto.freelancer.FreelancerProfileUpdateRequest;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.security.payload.response.MessageResponse;
import com.clickcraft.demo.security.services.UserDetailsImpl;
import com.clickcraft.demo.service.FreelancerProfileService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/freelancer")
public class FreelancerController {

    private static final Logger logger = LoggerFactory.getLogger(FreelancerController.class);

    private final FreelancerProfileService freelancerProfileService;

    @Autowired
    public FreelancerController(FreelancerProfileService freelancerProfileService) {
        this.freelancerProfileService = freelancerProfileService;
    }

    @PostMapping("/update")
    public ResponseEntity<MessageResponse> updateFreelancerProfile(@Valid @RequestBody FreelancerProfileUpdateRequest freelancerProfileUpdateRequest, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = freelancerProfileService.getFreelancerByEmail(userDetails.getEmail());

        if (user == null) {
            logger.error("User not found: {}", userDetails.getEmail());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            freelancerProfileService.updateFreelancerProfile(user, freelancerProfileUpdateRequest);

            logger.info("Freelancer profile updated successfully for user: {}", userDetails.getEmail());
            return ResponseEntity.ok(new MessageResponse("Freelancer profile updated successfully!"));
        } catch (Exception e) {
            logger.error("Error updating Freelancer profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<FreelancerProfileDTO> getFreelancerProfile(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = freelancerProfileService.getFreelancerByEmail(userDetails.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        FreelancerProfileDTO freelancerProfileDTO = FreelancerProfileDTO.fromUser(user);
        return ResponseEntity.ok(freelancerProfileDTO);
    }

    @GetMapping("/getAllFreelancers")
    public ResponseEntity<List<FreelancerProfileDTO>> getAllPublicProfiles() {
        try {
            List<FreelancerProfileDTO> publicProfiles = freelancerProfileService.getAllPublicProfiles();
            return ResponseEntity.ok(publicProfiles);
        } catch (Exception e) {
            logger.error("Error fetching all public profiles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/profile-pictures")
    public ResponseEntity<List<String>> getProfilePictures(@RequestParam List<Long> freelancerIds) {
        try {
            List<String> profilePictures = freelancerProfileService.getProfilePictures(freelancerIds);
            return ResponseEntity.ok(profilePictures);
        } catch (Exception e) {
            logger.error("Error fetching profile pictures", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{freelancerId}")
    public ResponseEntity<FreelancerProfileDTO> getPublicProfileById(@PathVariable String freelancerId) {
        try {
            Long id = Long.valueOf(freelancerId);
            FreelancerProfileDTO publicProfile = freelancerProfileService.getPublicProfileById(id);
            byte[] profilePictureData = freelancerProfileService.getProfilePictureData(id);
            publicProfile.setProfilePictureData(profilePictureData);
            return ResponseEntity.ok(publicProfile);
        } catch (NumberFormatException e) {
            logger.error("Invalid freelancer ID: {}", freelancerId);
            return ResponseEntity.badRequest().build();
        } catch (ResourceNotFoundException e) {
            logger.error("Freelancer Profile not found with ID: {}", freelancerId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error fetching public profile by ID: {}", freelancerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<FreelancerProfileDTO>> searchProfiles(@RequestParam Map<String, String> params) {
        try {
            List<FreelancerProfileDTO> profiles = freelancerProfileService.searchProfiles(params);
            return ResponseEntity.ok(profiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}