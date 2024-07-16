package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.dto.client.ClientProfileUpdateRequest;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.repository.ClientProfileRepository;
import com.clickcraft.demo.security.repository.UserRepository;
import com.clickcraft.demo.service.ClientProfileService;
import com.clickcraft.demo.service.converter.ClientProfileConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientProfileServiceImpl implements ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;
    private final ClientProfileConverter clientProfileConverter;
    private final UserRepository userRepository;

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
        clientProfileConverter.updateClientProfile(clientProfile, clientProfileUpdateRequest);
    }

    @Override
    public ClientProfile getClientProfileByEmail(String email) {
        return clientProfileRepository.findByUserEmail(email);
    }
}