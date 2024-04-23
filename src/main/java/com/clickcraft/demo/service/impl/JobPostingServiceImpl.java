package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.JobApplication;
import com.clickcraft.demo.repository.FreelancerProfileRepository;
import com.clickcraft.demo.repository.JobApplicationRepository;
import com.clickcraft.demo.repository.JobPostingRepository;
import com.clickcraft.demo.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    private final JobApplicationRepository jobApplicationRepository;

    private final FreelancerProfileRepository freelancerProfileRepository;

    @Autowired
    public JobPostingServiceImpl(JobPostingRepository jobPostingRepository, JobApplicationRepository jobApplicationRepository, FreelancerProfileRepository freelancerProfileRepository) {
        this.jobPostingRepository = jobPostingRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.freelancerProfileRepository = freelancerProfileRepository;
    }

    @Override
    public ClientJobPosting saveJobPosting(ClientJobPosting jobPosting) {
        return jobPostingRepository.save(jobPosting);
    }

    @Override
    public List<ClientJobPosting> getAllJobPostings() {
        return jobPostingRepository.findAll();
    }

    @Override
    public List<ClientJobPosting> getClientJobPostings(ClientProfile clientProfile) {
        return jobPostingRepository.findByClientProfile(clientProfile);
    }

    @Override
    public int countLiveJobPostingsByClientProfile(ClientProfile clientProfile) {
        List<ClientJobPosting> liveJobPostings = jobPostingRepository.findByClientProfileAndArchived(clientProfile, false);
        return liveJobPostings.size();
    }

    @Override
    public int countArchivedJobPostingsByClientProfile(ClientProfile clientProfile) {
        List<ClientJobPosting> archivedJobPostings = jobPostingRepository.findByClientProfileAndArchived(clientProfile, true);
        return archivedJobPostings.size();
    }

    @Override
    public void deleteJobPosting(Long id) {
        jobPostingRepository.deleteById(id);
    }

}
