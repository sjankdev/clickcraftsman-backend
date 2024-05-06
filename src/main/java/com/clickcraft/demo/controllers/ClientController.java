package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.client.ClientProfileDTO;
import com.clickcraft.demo.dto.client.ClientProfileUpdateRequest;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.security.services.UserDetailsImpl;
import com.clickcraft.demo.service.ClientProfileService;
import com.clickcraft.demo.service.JobPostingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public ResponseEntity<?> getClientProfile() {
        Optional<UserDetailsImpl> userDetailsOpt = getUserDetails();
        if (userDetailsOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        UserDetailsImpl userDetails = userDetailsOpt.get();
        try {
            logger.info("Received a request to fetch client profile. Logged-in Client: {} (Email: {})", userDetails.getUsername(), userDetails.getEmail());
            User user = clientProfileService.getClientByEmail(userDetails.getEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
            }

            ClientProfileDTO clientProfileDTO = ClientProfileDTO.fromUser(user);
            logger.info("Client Profile Data: {}", clientProfileDTO);
            return ResponseEntity.ok(clientProfileDTO);
        } catch (UsernameNotFoundException ex) {
            logger.error("Client not found by email", ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        } catch (Exception e) {
            logger.error("Error fetching client profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }


    @PostMapping("/update")
    public ResponseEntity<?> updateClientProfile(@Valid @RequestBody ClientProfileUpdateRequest clientProfileUpdateRequest) {
        Optional<UserDetailsImpl> userDetailsOpt = getUserDetails();
        if (userDetailsOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        UserDetailsImpl userDetails = userDetailsOpt.get();
        User user = clientProfileService.getClientByEmail(userDetails.getEmail());
        if (user == null || user.getClientProfile() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client profile not found");
        }

        try {
            clientProfileService.updateClientProfile(user.getClientProfile(), clientProfileUpdateRequest);
            logger.info("Client profile updated successfully for client: {}", userDetails.getEmail());
            ClientProfileDTO updatedProfileDTO = ClientProfileDTO.fromUser(user);
            return ResponseEntity.ok(updatedProfileDTO);
        } catch (DataAccessException ex) {
            logger.error("Error updating client profile", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating client profile");
        } catch (Exception e) {
            logger.error("Unexpected error updating client profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error updating client profile");
        }
    }

    @GetMapping("/client-job-postings/live-count")
    public ResponseEntity<Integer> countLiveClientJobPostings() {
        Optional<String> userEmailOpt = getUserEmail();
        if (userEmailOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(0);
        }

        String userEmail = userEmailOpt.get();
        ClientProfile clientProfile = clientProfileService.getClientProfileByEmail(userEmail);
        if (clientProfile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }

        int liveJobPostingCount = jobPostingService.countLiveJobPostingsByClientProfile(clientProfile);
        return ResponseEntity.ok(liveJobPostingCount);
    }


    @GetMapping("/client-job-postings/archived-count")
    public ResponseEntity<Integer> countArchivedClientJobPostings() {
        Optional<String> userEmailOpt = getUserEmail();
        if (userEmailOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(0);
        }

        String userEmail = userEmailOpt.get();
        ClientProfile clientProfile = clientProfileService.getClientProfileByEmail(userEmail);
        if (clientProfile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }

        int archivedJobPostingCount = jobPostingService.countArchivedJobPostingsByClientProfile(clientProfile);
        return ResponseEntity.ok(archivedJobPostingCount);
    }


    private Optional<UserDetailsImpl> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            return Optional.of((UserDetailsImpl) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    private Optional<String> getUserEmail() {
        return getUserDetails().map(UserDetailsImpl::getEmail);
    }
}
