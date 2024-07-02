package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.dto.client.ClientProfileUpdateRequest;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.models.enums.ELocations;
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
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Client not found with email: " + email));
    }

    @Override
    public void saveClient(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void updateClientProfile(ClientProfile clientProfile, ClientProfileUpdateRequest clientProfileUpdateRequest) {
        clientProfile.setFirstName(clientProfileUpdateRequest.getFirstName());
        clientProfile.setLastName(clientProfileUpdateRequest.getLastName());
        clientProfile.setContactPhone(clientProfileUpdateRequest.getContactPhone());
        clientProfile.setLocation(ELocations.valueOf(clientProfileUpdateRequest.getLocation()));
        clientProfile.setCompanyName(clientProfileUpdateRequest.getCompanyName());
        clientProfile.setCompanyIndustry(clientProfileUpdateRequest.getCompanyIndustry());
        clientProfile.setCompanySize(clientProfileUpdateRequest.getCompanySize());
        clientProfile.setCompanyLocation(clientProfileUpdateRequest.getCompanyLocation());
        clientProfile.setWebsite(clientProfileUpdateRequest.getWebsite());
        clientProfile.setInstagram(clientProfileUpdateRequest.getInstagram());
        clientProfile.setLinkedin(clientProfileUpdateRequest.getLinkedin());
    }

    @Override
    public ClientProfile getClientProfileByEmail(String email) {
        return clientProfileRepository.findByUserEmail(email);
    }
}
