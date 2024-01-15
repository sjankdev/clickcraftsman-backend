package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.JobPostingRequest;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.payload.response.MessageResponse;
import com.clickcraft.demo.service.ClientProfileService;
import com.clickcraft.demo.service.JobPostingService;
import com.clickcraft.demo.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/jobpostings")
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

    @Autowired
    private ClientProfileService clientProfileService;

    @Autowired
    private SkillService skillService;

    @PostMapping("/post")
    public ResponseEntity<?> postJob(@RequestBody JobPostingRequest jobPostingRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().body(new MessageResponse("User not authenticated."));
        }

        String userEmail = authentication.getName();

        ClientProfile clientProfile = clientProfileService.getClientProfileByEmail(userEmail);

        if (clientProfile == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Client profile not found."));
        }

        ClientJobPosting jobPosting = new ClientJobPosting();
        jobPosting.setJobName(jobPostingRequest.getJobName());
        jobPosting.setDescription(jobPostingRequest.getDescription());
        jobPosting.setDatePosted(new Date());
        jobPosting.setClientProfile(clientProfile);

        List<Skill> requiredSkills = skillService.getSkillsByIds(jobPostingRequest.getRequiredSkillIds());
        jobPosting.setRequiredSkills(requiredSkills);

        clientProfile.postJob(jobPosting);

        return ResponseEntity.ok(new MessageResponse("Job posted successfully!"));
    }
}
