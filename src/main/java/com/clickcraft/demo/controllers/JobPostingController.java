package com.clickcraft.demo.controllers;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.payload.response.MessageResponse;
import com.clickcraft.demo.service.ClientProfileService;
import com.clickcraft.demo.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/jobpostings")
public class JobPostingController {

    @Autowired
    private  JobPostingService clientJobPostingService;

    @Autowired
    private  ClientProfileService clientProfileService;

    @PostMapping("/post")
    public ResponseEntity<?> postJob(@RequestBody ClientJobPosting jobPosting) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().body(new MessageResponse("User not authenticated."));
        }

        String userEmail = authentication.getName();

        ClientProfile clientProfile = clientProfileService.getClientProfileByEmail(userEmail);

        if (clientProfile == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Client profile not found."));
        }

        jobPosting.setClientProfile(clientProfile);

        clientJobPostingService.saveJobPosting(jobPosting);

        return ResponseEntity.ok(new MessageResponse("Job posted successfully!"));
    }

}
