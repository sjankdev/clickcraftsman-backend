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

    @NotBlank
    @Size(min = 2, max = 40)
    private String location;

    private String portfolio;

    private int yearsOfExperience;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "freelancerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set < FreelancerOfferJob > jobOffers = new HashSet < > ();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "freelancer_profile_skills", joinColumns = @JoinColumn(name = "freelancer_profile_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set < Skill > skills = new HashSet < > ();


    public void addSkill(Skill skill) {
        this.skills.add(skill);
    }

    public static FreelancerProfile createFromSignupRequestFreelancer(SignupRequest signUpRequest, User user) {
        FreelancerProfile freelancerProfile = new FreelancerProfile();
        freelancerProfile.setFirstName(signUpRequest.getFirstName());
        freelancerProfile.setLastName(signUpRequest.getLastName());
        freelancerProfile.setContactPhone(signUpRequest.getContactPhone());
        freelancerProfile.setLocation(signUpRequest.getLocation());
        freelancerProfile.setPortfolio(signUpRequest.getPortfolio());
        freelancerProfile.setYearsOfExperience(signUpRequest.getYearsOfExperience());
        freelancerProfile.setUser(user);
        return freelancerProfile;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
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

    public void setJobOffers(Set < FreelancerOfferJob > jobOffers) {
        this.jobOffers = jobOffers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set < FreelancerOfferJob > getJobOffers() {
        return jobOffers;
    }

    public void addJobOffer(FreelancerOfferJob jobOffer) {
        jobOffers.add(jobOffer);
        jobOffer.setFreelancerProfile(this);
    }

    public void removeJobOffer(FreelancerOfferJob jobOffer) {
        jobOffers.remove(jobOffer);
        jobOffer.setFreelancerProfile(null);
    }

    public Set < Skill > getSkills() {
        return skills;
    }

    public void setSkills(Set < Skill > skills) {
        this.skills = skills;
    }
}