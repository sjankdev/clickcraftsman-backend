package com.clickcraft.demo.service;

import com.clickcraft.demo.dto.JobPostingRequest;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.repository.ClientProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobPostingService {

    @Autowired
    private ClientProfileRepository clientProfileRepository;

    public void postJob(Long clientId, JobPostingRequest jobPostingRequest) {
        Optional<ClientProfile> optionalClientProfile = clientProfileRepository.findById(clientId);

        if (optionalClientProfile.isPresent()) {
            ClientProfile clientProfile = optionalClientProfile.get();
            clientProfile.postJob(jobPostingRequest);
            clientProfileRepository.save(clientProfile);
        } else {
            throw new RuntimeException("Client profile not found with ID: " + clientId);
        }
    }
}
