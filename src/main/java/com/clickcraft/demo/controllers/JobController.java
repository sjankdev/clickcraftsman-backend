package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.JobApplicationRequest;
import com.clickcraft.demo.dto.JobApplicationResponse;
import com.clickcraft.demo.dto.JobPostingRequest;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.JobApplication;
import com.clickcraft.demo.payload.response.MessageResponse;
import com.clickcraft.demo.repository.JobApplicationRepository;
import com.clickcraft.demo.repository.JobPostingRepository;
import com.clickcraft.demo.service.ClientProfileService;
import com.clickcraft.demo.service.FreelancerProfileService;
import com.clickcraft.demo.service.JobPostingService;
import com.clickcraft.demo.service.SkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/job")
public class JobController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private FreelancerProfileService freelancerProfileService;

    @Autowired
    private ClientProfileService clientProfileService;

    @Autowired
    private JobPostingService jobPostingService;

    @Autowired
    private SkillService skillService;

    public JobController() {
    }

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

        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        JobApplicationResponse response = JobApplicationResponse.fromEntity(savedJobApplication);

        response.setFreelancerId(freelancerProfile.getId());

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

    @GetMapping("/client-received-applications")
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

    @GetMapping("/job-applications/{jobId}")
    public ResponseEntity<List<JobApplicationResponse>> getJobApplicationsForJob(@PathVariable Long jobId) {
        try {
            logger.info("Fetching job applications for jobId: {}", jobId);

            List<JobApplication> jobApplications = jobApplicationRepository.findJobApplicationsByJobId(jobId);

            logger.info("Fetched {} job applications for jobId: {}", jobApplications.size(), jobId);

            List<JobApplicationResponse> responseList = jobApplications.stream()
                    .map(jobApplication -> {
                        JobApplicationResponse response = JobApplicationResponse.fromEntity(jobApplication);
                        FreelancerProfile freelancerProfile = jobApplication.getFreelancerProfile();

                        if (freelancerProfile != null) {
                            response.setFreelancerId(freelancerProfile.getId());
                            response.setFreelancerFirstName(freelancerProfile.getFirstName());
                            response.setFreelancerLastName(freelancerProfile.getLastName());
                        } else {
                            logger.info("Freelancer profile is null for job application with id: {}", jobApplication.getId());
                        }

                        return response;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            logger.error("Error fetching job applications for job {}", jobId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

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

    @GetMapping("/client-job-postings")
    public ResponseEntity<List<ClientJobPosting>> getClientJobPostings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        String userEmail = authentication.getName();

        ClientProfile clientProfile = clientProfileService.getClientProfileByEmail(userEmail);

        if (clientProfile == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<ClientJobPosting> clientJobPostings = jobPostingService.getClientJobPostings(clientProfile);

        return ResponseEntity.ok(clientJobPostings);
    }

}