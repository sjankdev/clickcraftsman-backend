package com.clickcraft.demo.models;

import com.clickcraft.demo.payload.request.SignupRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "clientProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set < ClientJobPosting > jobPostings = new HashSet < > ();

    public ClientProfile() {}

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

    public void setJobPostings(Set < ClientJobPosting > jobPostings) {
        this.jobPostings = jobPostings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set < ClientJobPosting > getJobPostings() {
        return jobPostings;
    }

    public void addJobPosting(ClientJobPosting jobPosting) {
        jobPostings.add(jobPosting);
    }

    public static ClientProfile createFromSignupRequestClient(SignupRequest signUpRequest, User user) {
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setFirstName(signUpRequest.getFirstName());
        clientProfile.setLastName(signUpRequest.getLastName());
        clientProfile.setContactPhone(signUpRequest.getContactPhone());
        clientProfile.setLocation(ELocations.valueOf(signUpRequest.getLocation()));
        clientProfile.setUser(user);
        user.setClientProfile(clientProfile);
        return clientProfile;
    }

}