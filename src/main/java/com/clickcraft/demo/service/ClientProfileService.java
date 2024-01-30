package com.clickcraft.demo.service;

import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.repository.ClientProfileRepository;
import com.clickcraft.demo.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientProfileService {

    @Autowired
    private  ClientProfileRepository clientProfileRepository;

    @Autowired
    private UserRepository userRepository;

    public User getClientByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found with email: " + email));
    }

    public void saveClient(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void postJob(ClientProfile clientProfile, ClientJobPosting jobPosting) {
        clientProfile.addJobPosting(jobPosting);
    }

    public ClientProfile getClientProfileByEmail(String email) {
        return clientProfileRepository.findByUserEmail(email);
    }
}