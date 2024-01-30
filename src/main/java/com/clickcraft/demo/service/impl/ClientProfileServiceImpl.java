package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.repository.ClientProfileRepository;
import com.clickcraft.demo.security.repository.UserRepository;
import com.clickcraft.demo.service.ClientProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientProfileServiceImpl implements ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;

    private final UserRepository userRepository;

    @Autowired
    public ClientProfileServiceImpl(ClientProfileRepository clientProfileRepository, UserRepository userRepository) {
        this.clientProfileRepository = clientProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public User getClientByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found with email: " + email));
    }

    @Override
    public void saveClient(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void postJob(ClientProfile clientProfile, ClientJobPosting jobPosting) {
        clientProfile.addJobPosting(jobPosting);
    }

    @Override
    public ClientProfile getClientProfileByEmail(String email) {
        return clientProfileRepository.findByUserEmail(email);
    }
}
