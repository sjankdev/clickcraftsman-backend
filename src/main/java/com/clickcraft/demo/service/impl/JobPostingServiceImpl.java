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

    @Autowired
    private JobPostingRepository jobPostingRepository;

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
}
