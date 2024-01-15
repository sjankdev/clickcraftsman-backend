package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.JobPostingRequest;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.payload.response.MessageResponse;
import com.clickcraft.demo.service.ClientProfileService;
import com.clickcraft.demo.service.JobPostingService;
import com.clickcraft.demo.service.SkillService;

import java.time.LocalDate;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/job-postings")
public class JobPostingController {

    private static final Logger log = Logger.getLogger(JobPostingController.class.getName());


    @Autowired
    private ClientProfileService clientProfileService;

    @Autowired
    private JobPostingService jobPostingService;

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
        jobPosting.setDatePosted(LocalDate.now());
        jobPosting.setClientProfile(clientProfile);

        List<Skill> requiredSkills = skillService.getSkillsByNames(jobPostingRequest.getRequiredSkillIds());

        jobPosting.setRequiredSkills(requiredSkills);

        clientProfileService.postJob(clientProfile, jobPosting);

        jobPostingService.saveJobPosting(jobPosting);

        return ResponseEntity.ok(new MessageResponse("Job posted successfully!"));
    }

}
