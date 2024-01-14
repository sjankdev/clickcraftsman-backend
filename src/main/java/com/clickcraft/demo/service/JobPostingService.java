package com.clickcraft.demo.service;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class JobPostingService {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    public ClientJobPosting saveJobPosting(ClientJobPosting jobPosting) {
        return jobPostingRepository.save(jobPosting);
    }
}
