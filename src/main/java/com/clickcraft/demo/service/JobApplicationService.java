package com.clickcraft.demo.service;

import com.clickcraft.demo.dto.job.JobApplicationRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface JobApplicationService {

    void applyForJob(Long jobId, String userEmail, MultipartFile resumeFile, JobApplicationRequest applicationRequest) throws IOException;

}
