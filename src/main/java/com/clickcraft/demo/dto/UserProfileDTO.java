package com.clickcraft.demo.dto;

import com.clickcraft.demo.models.ELocations;
import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.models.User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private String contactPhone;
    private ELocations location;
    private String portfolio;
    private int yearsOfExperience;
    private Set<String> skills;

    private UserProfileDTO() {
    }

    public static UserProfileDTO fromUser(User user) {
        UserProfileDTO userProfileDTO = new UserProfileDTO();

        if (user != null) {
            if (user.getClientProfile() != null) {
                userProfileDTO.setFirstName(user.getClientProfile().getFirstName());
                userProfileDTO.setLastName(user.getClientProfile().getLastName());
                userProfileDTO.setContactPhone(user.getClientProfile().getContactPhone());
                userProfileDTO.setLocation(user.getClientProfile().getLocation());
            } else if (user.getFreelancerProfile() != null) {
                userProfileDTO.setFirstName(user.getFreelancerProfile().getFirstName());
                userProfileDTO.setLastName(user.getFreelancerProfile().getLastName());
                userProfileDTO.setContactPhone(user.getFreelancerProfile().getContactPhone());
                userProfileDTO.setLocation(user.getFreelancerProfile().getLocation());
                userProfileDTO.setPortfolio(user.getFreelancerProfile().getPortfolio());
                userProfileDTO.setYearsOfExperience(user.getFreelancerProfile().getYearsOfExperience());
                userProfileDTO.setSkills(user.getFreelancerProfile().getSkills().stream().map(Skill::getSkillName).collect(Collectors.toSet()));
            }
        }
        return userProfileDTO;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public ELocations getLocation() {
        return location;
    }

    public void setLocation(ELocations location) {
        this.location = location;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public void setSkills(Set<String> skills) {
        this.skills = skills;
    }
}
