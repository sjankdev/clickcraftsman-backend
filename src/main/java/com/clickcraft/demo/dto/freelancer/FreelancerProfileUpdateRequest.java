package com.clickcraft.demo.dto.freelancer;

import com.clickcraft.demo.models.enums.ELocations;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class FreelancerProfileUpdateRequest {

    private String firstName;
    private String lastName;
    private String contactPhone;
    private ELocations location;
    private String portfolio;
    private int yearsOfExperience;
    private Set<String> skills;

    public FreelancerProfileUpdateRequest() {
    }
}