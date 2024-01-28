package com.clickcraft.demo.dto;

import com.clickcraft.demo.models.User;

public class UserProfileDTO {

    private String firstName;
    private String lastName;

    public UserProfileDTO() {
    }

    public static UserProfileDTO fromUser(User user) {
        UserProfileDTO userProfileDTO = new UserProfileDTO();

        if (user.getClientProfile() != null) {
            userProfileDTO.setFirstName(user.getClientProfile().getFirstName());
            userProfileDTO.setLastName(user.getClientProfile().getLastName());
        } else if (user.getFreelancerProfile() != null) {
            userProfileDTO.setFirstName(user.getFreelancerProfile().getFirstName());
            userProfileDTO.setLastName(user.getFreelancerProfile().getLastName());
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
}


