package com.clickcraft.demo.security.payload.request;

import java.util.Set;

import jakarta.validation.constraints.*;

public class SignupRequest {

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private Set < String > role;

    @NotBlank
    @Size(min = 3, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 20)
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 20)
    private String contactPhone;

    @NotBlank
    @Size(min = 2, max = 40)
    private String location;

    private String portfolio;

    private int yearsOfExperience;

    private Set < String > skills;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set < String > getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getLocation() {
        return location;
    }

    public Set < String > getSkills() {
        return skills;
    }

    public void setSkills(Set < String > skills) {
        this.skills = skills;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }
}