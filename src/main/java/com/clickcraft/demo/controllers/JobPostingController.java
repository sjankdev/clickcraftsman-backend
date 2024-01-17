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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/job-postings")
public class JobPostingController {

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

        ClientJobPosting jobPosting = new ClientJobPosting(jobPostingRequest.getJobName(), jobPostingRequest.getDescription(), clientProfile, LocalDate.now(), jobPostingRequest.getIsRemote(), jobPostingRequest.getLocation(), skillService.getSkillsByNames(jobPostingRequest.getRequiredSkillIds()));

        clientProfile.addJobPosting(jobPosting);

        jobPostingService.saveJobPosting(jobPosting);

        return ResponseEntity.ok(new MessageResponse("Job posted successfully!"));
    }

    @GetMapping("/getAllJobs")
    public List <ClientJobPosting> getAllJobs() {
        return jobPostingService.getAllJobPostings();
    }

}