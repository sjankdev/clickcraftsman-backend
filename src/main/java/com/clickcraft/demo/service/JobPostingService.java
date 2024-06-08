package com.clickcraft.demo.service;

import jakarta.validation.Valid;
import com.clickcraft.demo.dto.job.JobPostingRequest;
import com.clickcraft.demo.dto.job.JobPostingResponse;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;

import java.util.List;
import java.util.Map;

public interface JobPostingService {

    ClientJobPosting createClientJobPosting(JobPostingRequest jobPostingRequest, ClientProfile clientProfile);

    void saveJobPosting(@Valid ClientJobPosting jobPosting);

    List<JobPostingResponse> getAllJobPostings();

    List<ClientJobPosting> getClientJobPostings(ClientProfile clientProfile);

    int countLiveJobPostingsByClientProfile(ClientProfile clientProfile);

    int countArchivedJobPostingsByClientProfile(ClientProfile clientProfile);

    void deleteJobPosting(Long id);

    List<JobPostingResponse> searchJobs(Map<String, String> params);

}
