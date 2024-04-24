package com.clickcraft.demo.service;

import com.clickcraft.demo.dto.client.ClientProfileUpdateRequest;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.User;

public interface ClientProfileService {

    User getClientByEmail(String email);

    void saveClient(User user);

    void updateClientProfile(ClientProfile clientProfile, ClientProfileUpdateRequest clientProfileUpdateRequest);

    ClientProfile getClientProfileByEmail(String email);

}
