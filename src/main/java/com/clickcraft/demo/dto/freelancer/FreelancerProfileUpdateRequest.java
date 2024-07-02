package com.clickcraft.demo.dto.freelancer;

import com.clickcraft.demo.models.enums.ELocations;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class FreelancerProfileUpdateRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 20)
    private String lastName;

    @NotBlank
    @Pattern(regexp="^\\+381\\d{8,9}$", message="Please enter a valid Serbian phone number")
    private String contactPhone;

    @NotNull
    private Set<String> skills;

    @NotNull
    private ELocations location;

    private String portfolio;

    @PositiveOrZero
    private int yearsOfExperience;

    @NotBlank
    @Size(min = 100, max = 2000)
    private String aboutFreelancer;

    public FreelancerProfileUpdateRequest() {
    }
}