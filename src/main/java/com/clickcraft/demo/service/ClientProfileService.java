package com.clickcraft.demo.service;

import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.User;

public interface ClientProfileService {

    User getClientByEmail(String email);

    void saveClient(User user);

    ClientProfile getClientProfileByEmail(String email);

}
