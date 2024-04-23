package com.clickcraft.demo.models;

import com.clickcraft.demo.models.enums.ELocations;
import com.clickcraft.demo.security.payload.request.SignupRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "client_profiles")
public class ClientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 20)
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 20)
    private String contactPhone;

    @Enumerated(EnumType.STRING)
    private ELocations location;

    private String companyName;
    private String companyLocation;
    private String companySize;
    private String companyIndustry;
    private String linkedin;
    private String website;
    private String instagram;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "clientProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClientJobPosting> jobPostings = new HashSet<>();

    public ClientProfile() {
    }

    public void addJobPosting(ClientJobPosting jobPosting) {
        jobPostings.add(jobPosting);
    }

    public static ClientProfile createFromSignupRequestClient(SignupRequest signUpRequest, User user) {
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setFirstName(signUpRequest.getFirstName());
        clientProfile.setLastName(signUpRequest.getLastName());
        clientProfile.setContactPhone(signUpRequest.getContactPhone());
        clientProfile.setCompanyName(signUpRequest.getCompanyName());
        clientProfile.setCompanySize(signUpRequest.getCompanySize());
        clientProfile.setCompanyIndustry(signUpRequest.getCompanyIndustry());
        clientProfile.setCompanyLocation(signUpRequest.getCompanyLocation());
        clientProfile.setWebsite(signUpRequest.getWebsite());
        clientProfile.setInstagram(signUpRequest.getInstagram());
        clientProfile.setLinkedin(signUpRequest.getLinkedin());
        clientProfile.setLocation(ELocations.valueOf(signUpRequest.getLocation()));
        clientProfile.setUser(user);
        user.setClientProfile(clientProfile);
        return clientProfile;
    }
}