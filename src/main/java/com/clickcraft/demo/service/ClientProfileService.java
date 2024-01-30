package com.clickcraft.demo.service;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.User;

public interface ClientProfileService {
    User getClientByEmail(String email);

    void saveClient(User user);

    void postJob(ClientProfile clientProfile, ClientJobPosting jobPosting);

    ClientProfile getClientProfileByEmail(String email);
}
