package com.clickcraft.demo.dto.client;

import com.clickcraft.demo.models.enums.ELocations;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ClientProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String contactPhone;
    private ELocations location;
    private String companyName;
    private String companyLocation;
    private String companySize;
    private String companyIndustry;
    private String linkedin;
    private String website;
    private String instagram;

    public ClientProfileUpdateRequest() {
    }
}