package com.clickcraft.demo.dto.client;

import com.clickcraft.demo.models.User;
import com.clickcraft.demo.models.enums.ELocations;

public class ClientProfileDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String contactPhone;
    private ELocations location;

    public ClientProfileDTO() {

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

    public static ClientProfileDTO fromUser(User user) {
        ClientProfileDTO clientProfileDTO = new ClientProfileDTO();

        if (user != null) {
            if (user.getClientProfile() != null) {
                clientProfileDTO.setFirstName(user.getClientProfile().getFirstName());
                clientProfileDTO.setLastName(user.getClientProfile().getLastName());
                clientProfileDTO.setContactPhone(user.getClientProfile().getContactPhone());
                clientProfileDTO.setLocation(user.getClientProfile().getLocation());
            }
        }
        return clientProfileDTO;
    }
}