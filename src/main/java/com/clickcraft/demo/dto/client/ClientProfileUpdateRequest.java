package com.clickcraft.demo.dto.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientProfileUpdateRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 20)
    private String lastName;

    @NotBlank
    @Pattern(regexp="^\\+381\\d{8,9}$", message="Please enter a valid Serbian phone number")
    private String contactPhone;

    @NotBlank
    @Size(max = 40)
    private String location;

    private String companyName;
    private String companyLocation;
    private String companySize;
    private String companyIndustry;

    @URL
    private String linkedin;

    @URL
    private String website;

    @URL
    private String instagram;

    public ClientProfileUpdateRequest() {
    }
}