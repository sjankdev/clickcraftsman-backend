package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.client.ClientProfileDTO;
import com.clickcraft.demo.dto.client.ClientProfileUpdateRequest;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.security.payload.response.MessageResponse;
import com.clickcraft.demo.security.services.UserDetailsImpl;
import com.clickcraft.demo.service.ClientProfileService;
import com.clickcraft.demo.service.JobPostingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/client")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientProfileService clientProfileService;
    private final JobPostingService jobPostingService;

    @Autowired
    public ClientController(ClientProfileService clientProfileService, JobPostingService jobPostingService) {
        this.clientProfileService = clientProfileService;
        this.jobPostingService = jobPostingService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ClientProfileDTO> getClientProfile() {
        try {
            UserDetailsImpl userDetails = getUserDetails();
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            logger.info("Received a request to fetch client profile. Logged-in Client: {} (Email: {})", userDetails.getUsername(), userDetails.getEmail());
            User user = clientProfileService.getClientByEmail(userDetails.getEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            ClientProfileDTO clientProfileDTO = ClientProfileDTO.fromUser(user);

            logger.info("Client Profile Data: {}", clientProfileDTO);
            return ResponseEntity.ok(clientProfileDTO);
        } catch (Exception e) {
            logger.error("Error fetching client profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<MessageResponse> updateClientProfile(@Valid @RequestBody ClientProfileUpdateRequest clientProfileUpdateRequest) {
        try {
            UserDetailsImpl userDetails = getUserDetails();
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            User user = clientProfileService.getClientByEmail(userDetails.getEmail());
            if (user == null || user.getClientProfile() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            clientProfileService.updateClientProfile(user.getClientProfile(), clientProfileUpdateRequest);

            logger.info("Client profile updated successfully for client: {}", userDetails.getEmail());
            return ResponseEntity.ok(new MessageResponse("User profile updated successfully!"));
        } catch (Exception e) {
            logger.error("Error updating client profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/client-job-postings/live-count")
    public ResponseEntity<Integer> countLiveClientJobPostings() {
        String userEmail = getUserEmail();
        if (userEmail == null) {
            return ResponseEntity.badRequest().body(0);
        }

        ClientProfile clientProfile = clientProfileService.getClientProfileByEmail(userEmail);
        if (clientProfile == null) {
            return ResponseEntity.badRequest().body(0);
        }

        int liveJobPostingCount = jobPostingService.countLiveJobPostingsByClientProfile(clientProfile);
        return ResponseEntity.ok(liveJobPostingCount);
    }

    @GetMapping("/client-job-postings/archived-count")
    public ResponseEntity<Integer> countArchivedClientJobPostings() {
        String userEmail = getUserEmail();
        if (userEmail == null) {
            return ResponseEntity.badRequest().body(0);
        }

        ClientProfile clientProfile = clientProfileService.getClientProfileByEmail(userEmail);
        if (clientProfile == null) {
            return ResponseEntity.badRequest().body(0);
        }

        int archivedJobPostingCount = jobPostingService.countArchivedJobPostingsByClientProfile(clientProfile);
        return ResponseEntity.ok(archivedJobPostingCount);
    }

    private UserDetailsImpl getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) ?
                (UserDetailsImpl) authentication.getPrincipal() : null;
    }

    private String getUserEmail() {
        UserDetailsImpl userDetails = getUserDetails();
        return userDetails != null ? userDetails.getEmail() : null;
    }
}
