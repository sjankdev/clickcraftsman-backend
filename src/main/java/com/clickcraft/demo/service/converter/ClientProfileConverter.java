package com.clickcraft.demo.service.converter;

import com.clickcraft.demo.dto.client.ClientProfileUpdateRequest;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.enums.ELocations;
import org.springframework.stereotype.Component;

@Component
public class ClientProfileConverter {

    public void updateClientProfile(ClientProfile clientProfile, ClientProfileUpdateRequest updateRequest) {
        clientProfile.setFirstName(updateRequest.getFirstName());
        clientProfile.setLastName(updateRequest.getLastName());
        clientProfile.setContactPhone(updateRequest.getContactPhone());
        clientProfile.setLocation(ELocations.valueOf(updateRequest.getLocation()));
        clientProfile.setCompanyName(updateRequest.getCompanyName());
        clientProfile.setCompanyIndustry(updateRequest.getCompanyIndustry());
        clientProfile.setCompanySize(updateRequest.getCompanySize());
        clientProfile.setCompanyLocation(updateRequest.getCompanyLocation());
        clientProfile.setWebsite(updateRequest.getWebsite());
        clientProfile.setInstagram(updateRequest.getInstagram());
        clientProfile.setLinkedin(updateRequest.getLinkedin());
    }
}
