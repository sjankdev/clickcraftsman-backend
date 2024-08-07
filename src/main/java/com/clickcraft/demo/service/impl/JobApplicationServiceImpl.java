package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.dto.job.JobApplicationRequest;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.JobApplication;
import com.clickcraft.demo.repository.JobApplicationRepository;
import com.clickcraft.demo.repository.JobPostingRepository;
import com.clickcraft.demo.service.FreelancerProfileService;
import com.clickcraft.demo.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(List.of("pdf", "doc", "docx", "txt"));
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024;

    private final FreelancerProfileService freelancerProfileService;
    private final JobPostingRepository jobPostingRepository;
    private final JobApplicationRepository jobApplicationRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyForJob(Long jobId, String userEmail, MultipartFile resumeFile, JobApplicationRequest applicationRequest) throws IOException {
        ClientJobPosting jobPosting = jobPostingRepository.findById(jobId).orElseThrow(() -> new IllegalArgumentException("Job posting not found with ID: " + jobId));

        FreelancerProfile freelancerProfile = freelancerProfileService.getFreelancerProfileByEmail(userEmail);
        if (freelancerProfile == null) {
            throw new IllegalArgumentException("Freelancer profile not found for email: " + userEmail);
        }

        JobApplication jobApplication = new JobApplication();
        jobApplication.setMessageToClient(applicationRequest.getMessageToClient());
        jobApplication.setDesiredPay(applicationRequest.getDesiredPay());
        jobApplication.setFreelancerProfile(freelancerProfile);
        jobApplication.setClientJobPosting(jobPosting);
        jobApplication.setApplicationTime(applicationRequest.getApplicationTime());

        if (resumeFile != null && !resumeFile.isEmpty()) {
            byte[] resumeBytes = resumeFile.getBytes();
            if (resumeBytes.length > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("File size exceeds the maximum allowed size.");
            }
            jobApplication.setResume(resumeBytes);
            jobApplication.setOriginalFileName(resumeFile.getOriginalFilename());
        }

        jobApplicationRepository.save(jobApplication);
    }

    private boolean isValidFileExtension(String fileExtension) {
        return ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase());
    }
}