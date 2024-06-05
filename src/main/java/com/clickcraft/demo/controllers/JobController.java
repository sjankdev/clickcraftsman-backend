package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.freelancer.FreelancerProfileDTO;
import com.clickcraft.demo.dto.job.JobApplicationRequest;
import com.clickcraft.demo.dto.job.JobApplicationResponse;
import com.clickcraft.demo.dto.job.JobPostingRequest;
import com.clickcraft.demo.dto.job.JobPostingResponse;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.JobApplication;
import com.clickcraft.demo.search.JobSearchCriteria;
import com.clickcraft.demo.security.payload.response.MessageResponse;
import com.clickcraft.demo.repository.JobApplicationRepository;
import com.clickcraft.demo.repository.JobPostingRepository;
import com.clickcraft.demo.service.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/job")
public class JobController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    private final JobPostingRepository jobPostingRepository;

    private final JobApplicationRepository jobApplicationRepository;

    private final FreelancerProfileService freelancerProfileService;

    private final ClientProfileService clientProfileService;

    private final JobPostingService jobPostingService;

    private final SkillService skillService;

    private final JobApplicationService jobApplicationService;

    @Autowired
    public JobController(JobPostingRepository jobPostingRepository, JobApplicationRepository jobApplicationRepository, FreelancerProfileService freelancerProfileService, ClientProfileService clientProfileService, JobPostingService jobPostingService, SkillService skillService, JobApplicationService jobApplicationService) {
        this.jobPostingRepository = jobPostingRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.freelancerProfileService = freelancerProfileService;
        this.clientProfileService = clientProfileService;
        this.jobPostingService = jobPostingService;
        this.skillService = skillService;
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping(value = "/apply/{jobId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> applyForJob(@PathVariable Long jobId, @RequestPart(value = "resumeFile", required = false) MultipartFile resumeFile, @Valid @ModelAttribute JobApplicationRequest applicationRequest, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }

        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();

        try {
            jobApplicationService.applyForJob(jobId, userEmail, resumeFile, applicationRequest);
            return ResponseEntity.ok("Job application submitted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process file upload", e);
        }
    }

    @GetMapping("/applied-jobs")
    public ResponseEntity<List<Long>> getAppliedJobs(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }

        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
        FreelancerProfile freelancerProfile = freelancerProfileService.getFreelancerProfileByEmail(userEmail);

        if (freelancerProfile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        List<Long> appliedJobIds = jobApplicationRepository.findAppliedJobIdsByFreelancerProfile(freelancerProfile);

        return ResponseEntity.ok(appliedJobIds);
    }

    @GetMapping("/client-received-applications")
    public ResponseEntity<List<JobApplicationResponse>> getClientJobApplications(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }

        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();

        ClientProfile clientProfile = clientProfileService.getClientProfileByEmail(userEmail);

        if (clientProfile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        List<JobApplication> clientJobApplications = jobApplicationRepository.findClientJobApplications(clientProfile);

        List<JobApplicationResponse> responseList = clientJobApplications.stream().map(JobApplicationResponse::fromEntity).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/resume/{jobApplicationId}")
    public ResponseEntity<byte[]> getResume(@PathVariable Long jobApplicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(jobApplicationId).orElse(null);
        if (jobApplication == null || jobApplication.getResume() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] resume = jobApplication.getResume();
        String originalFileName = jobApplication.getOriginalFileName() != null ? jobApplication.getOriginalFileName() : "resume.pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", originalFileName);
        headers.setContentLength(resume.length);

        return ResponseEntity.ok().headers(headers).body(resume);
    }

    @GetMapping("/job-applications/{jobId}")
    public ResponseEntity<List<JobApplicationResponse>> getJobApplicationsForJob(@PathVariable Long jobId) {
        try {
            logger.info("Fetching job applications for jobId: {}", jobId);

            List<JobApplication> jobApplications = jobApplicationRepository.findJobApplicationsByJobId(jobId);

            logger.info("Fetched {} job applications for jobId: {}", jobApplications.size(), jobId);

            List<JobApplicationResponse> responseList = jobApplications.stream().map(JobApplicationResponse::fromEntity).collect(Collectors.toList());

            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            logger.error("Error fetching job applications for job {}", jobId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/details/{jobId}")
    public ResponseEntity<JobPostingResponse> getJobDetails(@PathVariable Long jobId) {
        try {
            Optional<ClientJobPosting> jobPostingOptional = jobPostingRepository.findById(jobId);
            if (jobPostingOptional.isPresent()) {
                ClientJobPosting jobPosting = jobPostingOptional.get();
                JobPostingResponse response = JobPostingResponse.fromEntity(jobPosting);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            logger.error("Error fetching job details for job {}", jobId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/post")
    public ResponseEntity<MessageResponse> postJob(@RequestBody @Valid JobPostingRequest jobPostingRequest, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().body(new MessageResponse("User not authenticated."));
        }

        String userEmail = authentication.getName();
        ClientProfile clientProfile = clientProfileService.getClientProfileByEmail(userEmail);

        if (clientProfile == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Client profile not found."));
        }

        try {
            ClientJobPosting jobPosting = jobPostingService.createClientJobPosting(jobPostingRequest, clientProfile);
            jobPostingService.saveJobPosting(jobPosting);
            return ResponseEntity.ok(new MessageResponse("Job posted successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Failed to post job. Please try again later."));
        }
    }

    @GetMapping("/getAllJobs")
    public List<JobPostingResponse> getAllJobs() {
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteJobPosting(@PathVariable("id") Long id) {
        jobPostingService.deleteJobPosting(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/archive/{id}")
    public ResponseEntity<?> archiveJob(@PathVariable Long id) {
        Optional<ClientJobPosting> optionalJobPosting = jobPostingRepository.findById(id);
        if (optionalJobPosting.isPresent()) {
            ClientJobPosting jobPosting = optionalJobPosting.get();
            jobPosting.setArchived(true);
            jobPostingRepository.save(jobPosting);
            return ResponseEntity.ok(new MessageResponse("Job archived successfully."));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/unarchive/{id}")
    public ResponseEntity<?> unarchiveJob(@PathVariable Long id) {
        Optional<ClientJobPosting> optionalJobPosting = jobPostingRepository.findById(id);
        if (optionalJobPosting.isPresent()) {
            ClientJobPosting jobPosting = optionalJobPosting.get();
            jobPosting.setArchived(false);
            jobPostingRepository.save(jobPosting);
            return ResponseEntity.ok(new MessageResponse("Job unarchived successfully."));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/searchJobs")
    public ResponseEntity<List<JobPostingResponse>> searchJobs(@RequestParam(required = false) List<String> locations, @RequestParam(required = false) List<String> skillIds, @RequestParam(required = false) List<String> jobTypes, @RequestParam(required = false) List<String> priceTypes, @RequestParam(required = false) Double priceRangeFrom, @RequestParam(required = false) Double priceRangeTo, @RequestParam(required = false) Double budgetFrom, @RequestParam(required = false) Double budgetTo, @RequestParam(required = false) String jobName, @RequestParam(required = false) Boolean isRemote, @RequestParam(required = false) Boolean resumeRequired, @RequestParam(required = false) String dateRange) {
        try {
            JobSearchCriteria searchCriteria = new JobSearchCriteria().setLocations(locations).setSkillIds(skillIds).setJobTypes(jobTypes).setPriceTypes(priceTypes).setPriceRangeFrom(priceRangeFrom).setPriceRangeTo(priceRangeTo).setBudgetFrom(budgetFrom).setBudgetTo(budgetTo).setJobName(jobName).setIsRemote(isRemote).setResumeRequired(resumeRequired).setDateRange(dateRange);

            List<JobPostingResponse> profiles = jobPostingService.searchJobs(searchCriteria.toMap());

            return ResponseEntity.ok(profiles);
        } catch (Exception e) {
            logger.error("Error processing search jobs request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}