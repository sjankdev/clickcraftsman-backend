package com.clickcraft.demo.models;

import com.clickcraft.demo.models.enums.ELocations;
import com.clickcraft.demo.security.payload.request.SignupRequest;
import com.clickcraft.demo.utils.SerbiaMobilePhone;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
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
    @Pattern(regexp = "^\\+381\\d{8,9}$", message = "Please enter a valid Serbian phone number")
    private String contactPhone;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ELocations location;

    private String portfolio;

    @PositiveOrZero
    private int yearsOfExperience;

    @NotBlank
    @Size(min = 100, max = 2000)
    private String aboutFreelancer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "freelancer_profile_skills", joinColumns = @JoinColumn(name = "freelancer_profile_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills = new HashSet<>();

    @OneToMany(mappedBy = "freelancerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobApplication> jobApplications = new HashSet<>();

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