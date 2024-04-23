package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.repository.JobPostingRepository;
import com.clickcraft.demo.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    @Autowired
    public JobPostingServiceImpl(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    @Override
    public void saveJobPosting(ClientJobPosting jobPosting) {
        jobPostingRepository.save(jobPosting);
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
