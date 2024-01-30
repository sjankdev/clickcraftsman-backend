package com.clickcraft.demo.dto.freelancer;

import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.models.enums.ELocations;

import java.util.Set;
import java.util.stream.Collectors;

public class FreelancerProfileDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String contactPhone;
    private ELocations location;
    private String portfolio;
    private int yearsOfExperience;
    private Set<String> skills;

    public FreelancerProfileDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public static FreelancerProfileDTO fromUser(User user) {
        FreelancerProfileDTO freelancerProfileDTO = new FreelancerProfileDTO();

        if (user != null) {
            if (user.getClientProfile() != null) {
                freelancerProfileDTO.setFirstName(user.getClientProfile().getFirstName());
                freelancerProfileDTO.setLastName(user.getClientProfile().getLastName());
                freelancerProfileDTO.setContactPhone(user.getClientProfile().getContactPhone());
                freelancerProfileDTO.setLocation(user.getClientProfile().getLocation());
            } else if (user.getFreelancerProfile() != null) {
                freelancerProfileDTO.setFirstName(user.getFreelancerProfile().getFirstName());
                freelancerProfileDTO.setLastName(user.getFreelancerProfile().getLastName());
                freelancerProfileDTO.setContactPhone(user.getFreelancerProfile().getContactPhone());
                freelancerProfileDTO.setLocation(user.getFreelancerProfile().getLocation());
                freelancerProfileDTO.setPortfolio(user.getFreelancerProfile().getPortfolio());
                freelancerProfileDTO.setYearsOfExperience(user.getFreelancerProfile().getYearsOfExperience());
                freelancerProfileDTO.setSkills(user.getFreelancerProfile().getSkills().stream().map(Skill::getSkillName).collect(Collectors.toSet()));
            }
        }
        return freelancerProfileDTO;
    }
}
