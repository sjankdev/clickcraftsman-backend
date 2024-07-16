package com.clickcraft.demo.controllers;

import com.clickcraft.demo.constants.ErrorConstants;
import com.clickcraft.demo.dto.client.ClientProfileDTO;
import com.clickcraft.demo.dto.client.ClientProfileUpdateRequest;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.security.services.UserDetailsImpl;
import com.clickcraft.demo.security.services.UserDetailsServiceImpl;
import com.clickcraft.demo.service.ClientProfileService;
import com.clickcraft.demo.service.JobPostingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientProfileService clientProfileService;
    private final JobPostingService jobPostingService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @GetMapping("/profile")
    public ResponseEntity<ClientProfileDTO> getClientProfile() {
        Optional<UserDetailsImpl> userDetailsOpt = userDetailsServiceImpl.getUserDetails();
        if (userDetailsOpt.isEmpty()) {
            return ResponseEntity.status(ErrorConstants.HTTP_UNAUTHORIZED).build();
        }

        UserDetailsImpl userDetails = userDetailsOpt.get();
        try {
            logger.info("Received a request to fetch client profile. Logged-in Client: {} (Email: {})", userDetails.getUsername(), userDetails.getEmail());
            User user = clientProfileService.getClientByEmail(userDetails.getEmail());
            if (user == null) {
                return ResponseEntity.status(ErrorConstants.HTTP_NOT_FOUND).build();
            }

            ClientProfileDTO clientProfileDTO = ClientProfileDTO.fromUser(user);
            logger.info("Client Profile Data: {}", clientProfileDTO);
            return ResponseEntity.ok(clientProfileDTO);
        } catch (UsernameNotFoundException ex) {
            logger.error(ErrorConstants.ERROR_CLIENT_NOT_FOUND, ex);
            return ResponseEntity.status(ErrorConstants.HTTP_NOT_FOUND).build();
        } catch (Exception e) {
            logger.error(ErrorConstants.ERROR_UNEXPECTED, e);
            return ResponseEntity.status(ErrorConstants.HTTP_INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<ClientProfileDTO> updateClientProfile(@Valid @RequestBody ClientProfileUpdateRequest clientProfileUpdateRequest) {
        Optional<UserDetailsImpl> userDetailsOpt = userDetailsServiceImpl.getUserDetails();
        if (userDetailsOpt.isEmpty()) {
            return ResponseEntity.status(ErrorConstants.HTTP_UNAUTHORIZED).build();
        }

        UserDetailsImpl userDetails = userDetailsOpt.get();
        User user = clientProfileService.getClientByEmail(userDetails.getEmail());
        if (user == null || user.getClientProfile() == null) {
            return ResponseEntity.status(ErrorConstants.HTTP_NOT_FOUND).build();
        }

        try {
            clientProfileService.updateClientProfile(user.getClientProfile(), clientProfileUpdateRequest);
            logger.info("Client profile updated successfully for client: {}", userDetails.getEmail());
            ClientProfileDTO updatedProfileDTO = ClientProfileDTO.fromUser(user);
            return ResponseEntity.ok(updatedProfileDTO);
        } catch (DataAccessException ex) {
            logger.error(ErrorConstants.ERROR_CLIENT_UPDATE_FAILED, ex);
            return ResponseEntity.status(ErrorConstants.HTTP_INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            logger.error(ErrorConstants.ERROR_UNEXPECTED, e);
            return ResponseEntity.status(ErrorConstants.HTTP_INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/client-job-postings/live-count")
    public ResponseEntity<Integer> countLiveClientJobPostings() {
        Optional<String> userEmailOpt = userDetailsServiceImpl.getUserEmail();
        if (userEmailOpt.isEmpty()) {
            return ResponseEntity.status(ErrorConstants.HTTP_UNAUTHORIZED).build();
        }

        String userEmail = userEmailOpt.get();
        ClientProfile clientProfile = clientProfileService.getClientProfileByEmail(userEmail);
        if (clientProfile == null) {
            return ResponseEntity.status(ErrorConstants.HTTP_NOT_FOUND).build();
        }

        int liveJobPostingCount = jobPostingService.countLiveJobPostingsByClientProfile(clientProfile);
        return ResponseEntity.ok(liveJobPostingCount);
    }

    @GetMapping("/client-job-postings/archived-count")
    public ResponseEntity<Integer> countArchivedClientJobPostings() {
        Optional<String> userEmailOpt = userDetailsServiceImpl.getUserEmail();
        if (userEmailOpt.isEmpty()) {
            return ResponseEntity.status(ErrorConstants.HTTP_UNAUTHORIZED).build();
        }

        String userEmail = userEmailOpt.get();
        ClientProfile clientProfile = clientProfileService.getClientProfileByEmail(userEmail);
        if (clientProfile == null) {
            return ResponseEntity.status(ErrorConstants.HTTP_NOT_FOUND).build();
        }

        int archivedJobPostingCount = jobPostingService.countArchivedJobPostingsByClientProfile(clientProfile);
        return ResponseEntity.ok(archivedJobPostingCount);
    }
}
