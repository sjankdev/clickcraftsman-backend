package com.clickcraft.demo.models;

import com.clickcraft.demo.payload.request.SignupRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

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
        freelancerProfile.setUser(user);
        return freelancerProfile;
    }

    public Long getId() {
        return id;
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

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public Set<JobApplication> getJobApplications() {
        return jobApplications;
    }

    public void setJobApplications(Set<JobApplication> jobApplications) {
        this.jobApplications = jobApplications;
    }

}