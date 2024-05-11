package com.clickcraft.demo.security.payload.request;

import com.clickcraft.demo.utils.SerbiaMobilePhone;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotNull
    private Set<String> role;

    @NotBlank
    @Size(min = 3, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 20)
    private String lastName;

    @NotBlank
    @SerbiaMobilePhone
    @Size(min = 12, max = 12, message = "Serbian mobile phone number must be exactly 12 digits long including the country code")
    private String contactPhone;

    @NotBlank
    @Size(min = 2, max = 40)
    private String location;

    @NotBlank
    @Size(max = 2000)
    private String aboutFreelancer;

    private String portfolio;

    @Positive
    private int yearsOfExperience;

    @NotNull
    private Set<String> skills;

    private MultipartFile profilePicture;

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

}
