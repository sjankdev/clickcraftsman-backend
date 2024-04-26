package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.dto.job.JobApplicationRequest;
import com.clickcraft.demo.dto.job.JobApplicationResponse;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.JobApplication;
import com.clickcraft.demo.repository.JobApplicationRepository;
import com.clickcraft.demo.repository.JobPostingRepository;
import com.clickcraft.demo.service.FreelancerProfileService;
import com.clickcraft.demo.service.JobApplicationService;
import com.clickcraft.demo.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    private final FreelancerProfileService freelancerProfileService;
    private final JobPostingRepository jobPostingRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final StorageService storageService;

    @Autowired
    public JobApplicationServiceImpl(FreelancerProfileService freelancerProfileService,
                                 JobPostingRepository jobPostingRepository,
                                 JobApplicationRepository jobApplicationRepository,
                                 StorageService storageService) {
        this.freelancerProfileService = freelancerProfileService;
        this.jobPostingRepository = jobPostingRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.storageService = storageService;
    }

    public JobApplicationResponse applyForJob(Long jobId, String userEmail, MultipartFile resumeFile, JobApplicationRequest applicationRequest) throws IOException {
        FreelancerProfile freelancerProfile = freelancerProfileService.getFreelancerProfileByEmail(userEmail);
        if (freelancerProfile == null) {
            throw new RuntimeException("Freelancer profile not found for email: " + userEmail);
        }

        ClientJobPosting jobPosting = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job posting not found for jobId: " + jobId));

        if (jobApplicationRepository.existsByFreelancerProfileAndClientJobPosting(freelancerProfile, jobPosting)) {
            throw new RuntimeException("You have already applied for this job.");
        }

        String messageToClient = applicationRequest.getMessageToClient();
        Double desiredPay = applicationRequest.getDesiredPay();

        JobApplication jobApplication = new JobApplication();
        jobApplication.setClientJobPosting(jobPosting);
        jobApplication.setFreelancerProfile(freelancerProfile);
        jobApplication.setMessageToClient(messageToClient);
        jobApplication.setDesiredPay(desiredPay);

        if (resumeFile != null && !resumeFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(resumeFile.getOriginalFilename()));
            jobApplication.setOriginalFileName(fileName);
            jobApplication.setResume(storageService.storeFile(resumeFile, freelancerProfile.getId()));
        }

        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        JobApplicationResponse response = JobApplicationResponse.fromEntity(savedJobApplication);
        response.setFreelancerId(freelancerProfile.getId());

        return response;
    }
}
