package com.clickcraft.demo.dto.freelancer;

import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.models.enums.ELocations;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
public class FreelancerProfileDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String contactPhone;
    private ELocations location;
    private String portfolio;
    private int yearsOfExperience;
    private Set<String> skills;
    private byte[] profilePictureData;

    public FreelancerProfileDTO() {
    }

    public static FreelancerProfileDTO fromUser(User user) {
        FreelancerProfileDTO freelancerProfileDTO = new FreelancerProfileDTO();

        if (user != null) {
            if (user.getFreelancerProfile() != null) {
                freelancerProfileDTO.setFirstName(user.getFreelancerProfile().getFirstName());
                freelancerProfileDTO.setLastName(user.getFreelancerProfile().getLastName());
                freelancerProfileDTO.setContactPhone(user.getFreelancerProfile().getContactPhone());
                freelancerProfileDTO.setLocation(user.getFreelancerProfile().getLocation());
                freelancerProfileDTO.setPortfolio(user.getFreelancerProfile().getPortfolio());
                freelancerProfileDTO.setYearsOfExperience(user.getFreelancerProfile().getYearsOfExperience());
                freelancerProfileDTO.setSkills(user.getFreelancerProfile().getSkills().stream().map(Skill::getSkillName).collect(Collectors.toSet()));
                freelancerProfileDTO.setProfilePictureData(user.getProfilePictureData());
            }
        }
        return freelancerProfileDTO;
    }
}