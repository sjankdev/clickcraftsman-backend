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
@Table(name = "freelancers_profiles")
public class FreelancerProfile {

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

    private String portfolio;
    private int yearsOfExperience;

    @NotBlank
    @Size(max = 1000)
    private String aboutFreelancer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "freelancer_profile_skills", joinColumns = @JoinColumn(name = "freelancer_profile_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills = new HashSet<>();

    @OneToMany(mappedBy = "freelancerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobApplication> jobApplications = new HashSet<>();

    public FreelancerProfile() {
    }

    public void addSkill(Skill skill) {
        this.skills.add(skill);
    }

    public static FreelancerProfile createFromSignupRequestFreelancer(SignupRequest signUpRequest, User user) {
        FreelancerProfile freelancerProfile = new FreelancerProfile();
        freelancerProfile.setFirstName(signUpRequest.getFirstName());
        freelancerProfile.setLastName(signUpRequest.getLastName());
        freelancerProfile.setContactPhone(signUpRequest.getContactPhone());
        freelancerProfile.setLocation(ELocations.valueOf(signUpRequest.getLocation()));
        freelancerProfile.setPortfolio(signUpRequest.getPortfolio());
        freelancerProfile.setYearsOfExperience(signUpRequest.getYearsOfExperience());
        freelancerProfile.setAboutFreelancer(signUpRequest.getAboutFreelancer());
        freelancerProfile.setUser(user);
        return freelancerProfile;
    }
}