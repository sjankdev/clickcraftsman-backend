package com.clickcraft.demo.service;

import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.repository.ClientProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;

    @Autowired
    public ClientProfileService(ClientProfileRepository clientProfileRepository) {
        this.clientProfileRepository = clientProfileRepository;
    }

    @Transactional
    public void postJob(ClientProfile clientProfile, ClientJobPosting jobPosting) {
        clientProfile.addJobPosting(jobPosting);
    }

    public ClientProfile getClientProfileByEmail(String email) {
        return clientProfileRepository.findByUserEmail(email);
    }
}