package com.clickcraft.demo.service;

import com.clickcraft.demo.dto.job.JobPostingRequest;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;

import java.util.List;

public interface JobPostingService {

    void saveJobPosting(ClientJobPosting jobPosting);

    List<ClientJobPosting> getAllJobPostings();

    List<ClientJobPosting> getClientJobPostings(ClientProfile clientProfile);

    int countLiveJobPostingsByClientProfile(ClientProfile clientProfile);

    int countArchivedJobPostingsByClientProfile(ClientProfile clientProfile);

    void deleteJobPosting(Long id);

    ClientJobPosting createClientJobPosting(JobPostingRequest jobPostingRequest, ClientProfile clientProfile);

}
