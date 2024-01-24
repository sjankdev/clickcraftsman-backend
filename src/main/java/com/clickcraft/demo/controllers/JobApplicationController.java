package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.JobApplicationRequest;
import com.clickcraft.demo.dto.JobApplicationResponse;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.JobApplication;
import com.clickcraft.demo.payload.response.MessageResponse;
import com.clickcraft.demo.repository.JobApplicationRepository;
import com.clickcraft.demo.repository.JobPostingRepository;
import com.clickcraft.demo.service.ClientProfileService;
import com.clickcraft.demo.service.FreelancerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/job-applications")
public class JobApplicationController {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private FreelancerProfileService freelancerProfileService;

    @Autowired
    private ClientProfileService clientProfileService;

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<?> applyForJob(@PathVariable Long jobId, @RequestBody JobApplicationRequest applicationRequest, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.badRequest().body(new MessageResponse("User not authenticated."));
        }

        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();

        FreelancerProfile freelancerProfile = freelancerProfileService.getFreelancerProfileByEmail(userEmail);

        if (freelancerProfile == null) {
            System.out.println("Freelancer profile not found for email: " + userEmail);
            return ResponseEntity.badRequest().body(new MessageResponse("Freelancer profile not found."));
        }

        ClientJobPosting jobPosting = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job posting not found for jobId: " + jobId));

        boolean hasApplied = jobApplicationRepository.existsByFreelancerProfileAndClientJobPosting(freelancerProfile, jobPosting);

        if (hasApplied) {
            return ResponseEntity.badRequest().body(new MessageResponse("You have already applied for this job."));
        }

        String messageToClient = applicationRequest.getMessageToClient();

        JobApplication jobApplication = new JobApplication();
        jobApplication.setClientJobPosting(jobPosting);
        jobApplication.setFreelancerProfile(freelancerProfile);
        jobApplication.setMessageToClient(messageToClient);

        jobApplicationRepository.save(jobApplication);

        return ResponseEntity.ok("Job application submitted successfully");
    }

    @GetMapping("/applied-jobs")
    public ResponseEntity<List<Long>> getAppliedJobs(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
        FreelancerProfile freelancerProfile = freelancerProfileService.getFreelancerProfileByEmail(userEmail);

        if (freelancerProfile == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<Long> appliedJobIds = jobApplicationRepository.findAppliedJobIdsByFreelancerProfile(freelancerProfile);

        return ResponseEntity.ok(appliedJobIds);
    }

    @GetMapping("/client-job-applications")
    public ResponseEntity<List<JobApplicationResponse>> getClientJobApplications(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();

        ClientProfile clientProfile = clientProfileService.getClientProfileByEmail(userEmail);

        if (clientProfile == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<JobApplication> clientJobApplications = jobApplicationRepository.findClientJobApplications(clientProfile);

        List<JobApplicationResponse> responseList = clientJobApplications.stream()
                .map(jobApplication -> {
                    JobApplicationResponse response = JobApplicationResponse.fromEntity(jobApplication);
                    response.setFreelancerFirstName(jobApplication.getFreelancerProfile().getFirstName());
                    response.setFreelancerLastName(jobApplication.getFreelancerProfile().getLastName());
                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }



}