package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.dto.job.JobApplicationRequest;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.JobApplication;
import com.clickcraft.demo.repository.JobApplicationRepository;
import com.clickcraft.demo.repository.JobPostingRepository;
import com.clickcraft.demo.service.FreelancerProfileService;
import com.clickcraft.demo.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(List.of("pdf", "doc", "docx", "txt"));

    private final FreelancerProfileService freelancerProfileService;
    private final JobPostingRepository jobPostingRepository;
    private final JobApplicationRepository jobApplicationRepository;

    @Autowired
    public JobApplicationServiceImpl(FreelancerProfileService freelancerProfileService, JobPostingRepository jobPostingRepository, JobApplicationRepository jobApplicationRepository) {
        this.freelancerProfileService = freelancerProfileService;
        this.jobPostingRepository = jobPostingRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Override
    public void applyForJob(Long jobId, String userEmail, MultipartFile resumeFile, JobApplicationRequest applicationRequest) throws IOException {
        Optional<ClientJobPosting> jobPostingOptional = jobPostingRepository.findById(jobId);
        if (jobPostingOptional.isEmpty()) {
            throw new IllegalArgumentException("Job posting not found with ID: " + jobId);
        }

        ClientJobPosting jobPosting = jobPostingOptional.get();

        FreelancerProfile freelancerProfile = freelancerProfileService.getFreelancerProfileByEmail(userEmail);
        if (freelancerProfile == null) {
            throw new IllegalArgumentException("Freelancer profile not found for email: " + userEmail);
        }

        JobApplication jobApplication = new JobApplication();
        jobApplication.setMessageToClient(applicationRequest.getMessageToClient());
        jobApplication.setDesiredPay(applicationRequest.getDesiredPay());
        jobApplication.setFreelancerProfile(freelancerProfile);
        jobApplication.setClientJobPosting(jobPosting);

        if (resumeFile != null && !resumeFile.isEmpty()) {
            jobApplication.setResume(resumeFile.getBytes());
            jobApplication.setOriginalFileName(resumeFile.getOriginalFilename());
        }

        jobApplicationRepository.save(jobApplication);
    }

    private boolean isValidFileExtension(String fileExtension) {
        return ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase());
    }
}
