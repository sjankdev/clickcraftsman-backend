package com.clickcraft.demo.models;

import com.clickcraft.demo.payload.request.SignupRequest;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "worker_profiles")
public class WorkerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String contactPhone;

    private String location;

    private String skills;

    private String portfolio;

    private int yearsOfExperience;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkerOfferJob> jobOffers = new HashSet<>();

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

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
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

    public void setJobOffers(Set<WorkerOfferJob> jobOffers) {
        this.jobOffers = jobOffers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<WorkerOfferJob> getJobOffers() {
        return jobOffers;
    }

    public void addJobOffer(WorkerOfferJob jobOffer) {
        jobOffers.add(jobOffer);
        jobOffer.setWorkerProfile(this);
    }

    public void removeJobOffer(WorkerOfferJob jobOffer) {
        jobOffers.remove(jobOffer);
        jobOffer.setWorkerProfile(null);
    }

    public static WorkerProfile createFromSignupRequestWorker(SignupRequest signUpRequest, User user) {
        WorkerProfile workerProfile = new WorkerProfile();
        workerProfile.setFirstName(signUpRequest.getFirstName());
        workerProfile.setLastName(signUpRequest.getLastName());
        workerProfile.setContactPhone(signUpRequest.getContactPhone());
        workerProfile.setLocation(signUpRequest.getLocation());
        workerProfile.setSkills(signUpRequest.getSkills());
        workerProfile.setPortfolio(signUpRequest.getPortfolio());
        workerProfile.setYearsOfExperience(signUpRequest.getYearsOfExperience());
        workerProfile.setUser(user);
        return workerProfile;
    }
}
