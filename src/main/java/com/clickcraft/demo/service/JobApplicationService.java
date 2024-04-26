package com.clickcraft.demo.service;

import com.clickcraft.demo.dto.job.JobApplicationRequest;
import com.clickcraft.demo.dto.job.JobApplicationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface JobApplicationService {

    JobApplicationResponse applyForJob(Long jobId, String userEmail, MultipartFile resumeFile, JobApplicationRequest applicationRequest) throws IOException;

}
