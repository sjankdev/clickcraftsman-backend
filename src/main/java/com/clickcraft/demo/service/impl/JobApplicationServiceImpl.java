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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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

        String fileExtension = StringUtils.getFilenameExtension(resumeFile.getOriginalFilename());
        assert fileExtension != null;
        if (!isValidFileExtension(fileExtension)) {
            throw new RuntimeException("Invalid file format. Only " + String.join(", ", ALLOWED_EXTENSIONS) + " files are accepted.");
        }

        JobApplication jobApplication = new JobApplication();
        jobApplication.setClientJobPosting(jobPosting);
        jobApplication.setFreelancerProfile(freelancerProfile);
        jobApplication.setMessageToClient(messageToClient);
        jobApplication.setDesiredPay(desiredPay);

        if (!resumeFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(resumeFile.getOriginalFilename()));
            jobApplication.setOriginalFileName(fileName);
            jobApplication.setResume(resumeFile.getBytes());
        }

        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        JobApplicationResponse response = JobApplicationResponse.fromEntity(savedJobApplication);
        response.setFreelancerId(freelancerProfile.getId());

        return response;
    }

    private boolean isValidFileExtension(String fileExtension) {
        return ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase());
    }
}
