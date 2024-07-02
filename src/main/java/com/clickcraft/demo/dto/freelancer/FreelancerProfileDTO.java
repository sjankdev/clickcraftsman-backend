package com.clickcraft.demo.dto.freelancer;

import com.clickcraft.demo.models.FreelancerProfile;
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
    private String aboutFreelancer;

    public FreelancerProfileDTO() {
    }

    public static FreelancerProfileDTO fromUser(User user) {
        FreelancerProfileDTO freelancerProfileDTO = new FreelancerProfileDTO();

        if (user != null && user.getFreelancerProfile() != null) {
            FreelancerProfile freelancerProfile = user.getFreelancerProfile();
            freelancerProfileDTO.setId(freelancerProfile.getId());
            freelancerProfileDTO.setFirstName(freelancerProfile.getFirstName());
            freelancerProfileDTO.setLastName(freelancerProfile.getLastName());
            freelancerProfileDTO.setContactPhone(freelancerProfile.getContactPhone());
            freelancerProfileDTO.setLocation(freelancerProfile.getLocation());
            freelancerProfileDTO.setPortfolio(freelancerProfile.getPortfolio());
            freelancerProfileDTO.setYearsOfExperience(freelancerProfile.getYearsOfExperience());
            freelancerProfileDTO.setSkills(freelancerProfile.getSkills().stream().map(Skill::getSkillName).collect(Collectors.toSet()));
            freelancerProfileDTO.setProfilePictureData(user.getProfilePictureData());
            freelancerProfileDTO.setAboutFreelancer(freelancerProfile.getAboutFreelancer());
        }

        return freelancerProfileDTO;
    }
}