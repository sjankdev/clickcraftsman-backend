package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.UserProfileDTO;
import com.clickcraft.demo.dto.UserProfileUpdateRequest;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.payload.response.MessageResponse;
import com.clickcraft.demo.security.services.UserDetailsImpl;
import com.clickcraft.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            System.out.println("Received a request to fetch user profile. Principal class: " + principal.getClass());

            if (principal instanceof UserDetailsImpl) {
                UserDetailsImpl userDetails = (UserDetailsImpl) principal;
                System.out.println("Logged-in User: " + userDetails.getUsername() + " (Email: " + userDetails.getEmail() + ")");

                User user = userService.getUserByEmail(userDetails.getEmail());

                UserProfileDTO userProfileDTO = UserProfileDTO.fromUser(user);
                System.out.println("User Profile Data: " + userProfileDTO);
                return ResponseEntity.ok(userProfileDTO);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/update")
    public ResponseEntity<MessageResponse> updateUserProfile(@RequestBody UserProfileUpdateRequest updateRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getEmail());

        if (user.getClientProfile() != null) {
            user.getClientProfile().setFirstName(updateRequest.getFirstName());
            user.getClientProfile().setLastName(updateRequest.getLastName());
        } else if (user.getFreelancerProfile() != null) {
            user.getFreelancerProfile().setFirstName(updateRequest.getFirstName());
            user.getFreelancerProfile().setLastName(updateRequest.getLastName());
        }

        userService.saveUser(user);

        return ResponseEntity.ok(new MessageResponse("User profile updated successfully!"));
    }
}
