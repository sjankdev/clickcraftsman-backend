package com.clickcraft.demo.service;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;

import java.util.List;

public interface JobPostingService {

    ClientJobPosting saveJobPosting(ClientJobPosting jobPosting);

    List<ClientJobPosting> getAllJobPostings();

    List<ClientJobPosting> getClientJobPostings(ClientProfile clientProfile);

}
