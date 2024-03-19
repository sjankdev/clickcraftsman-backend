package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.client.ClientProfileDTO;
import com.clickcraft.demo.dto.client.ClientProfileUpdateRequest;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.repository.JobApplicationRepository;
import com.clickcraft.demo.security.payload.response.MessageResponse;
import com.clickcraft.demo.security.services.UserDetailsImpl;
import com.clickcraft.demo.service.ClientProfileService;
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

    @Autowired
    public ClientController(ClientProfileService clientProfileService) {
        this.clientProfileService = clientProfileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ClientProfileDTO> getClientProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
                logger.info("Received a request to fetch client profile. Logged-in Client: {} (Email: {})", userDetails.getUsername(), userDetails.getEmail());

                User user = clientProfileService.getClientByEmail(userDetails.getEmail());

                if (user.getProfilePictureData() != null) {
                    logger.info("Profile picture data retrieved for client: {}", userDetails.getEmail());
                } else {
                    logger.warn("Profile picture data not found for client: {}", userDetails.getEmail());
                }

                ClientProfileDTO clientProfileDTO = ClientProfileDTO.fromUser(user);

                logger.info("Client Profile Data: {}", clientProfileDTO);
                return ResponseEntity.ok(clientProfileDTO);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Error fetching client profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<MessageResponse> updateClientProfile(@RequestBody ClientProfileUpdateRequest clientProfileUpdateRequest) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = clientProfileService.getClientByEmail(userDetails.getEmail());

            updateClientProfileData(user, clientProfileUpdateRequest);

            clientProfileService.saveClient(user);

            logger.info("Client profile updated successfully for client: {}", userDetails.getEmail());
            return ResponseEntity.ok(new MessageResponse("User profile updated successfully!"));
        } catch (Exception e) {
            logger.error("Error updating client profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void updateClientProfileData(User user, ClientProfileUpdateRequest clientProfileUpdateRequest) {
        if (user.getClientProfile() != null) {
            user.getClientProfile().setFirstName(clientProfileUpdateRequest.getFirstName());
            user.getClientProfile().setLastName(clientProfileUpdateRequest.getLastName());
            user.getClientProfile().setContactPhone(clientProfileUpdateRequest.getContactPhone());
            user.getClientProfile().setLocation(clientProfileUpdateRequest.getLocation());
        }
    }
}