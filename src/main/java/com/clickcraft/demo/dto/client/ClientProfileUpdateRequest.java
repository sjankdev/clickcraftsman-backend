package com.clickcraft.demo.dto.client;

import com.clickcraft.demo.models.enums.ELocations;

import java.util.Set;

public class ClientProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String contactPhone;
    private ELocations location;
    public ClientProfileUpdateRequest() {
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
}
