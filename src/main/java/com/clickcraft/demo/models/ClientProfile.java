package com.clickcraft.demo.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "client_profiles")
public class ClientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String contactPhone;

    private String location;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "clientProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClientJobPosting> jobPostings = new HashSet<>();

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

    public void setJobPostings(Set<ClientJobPosting> jobPostings) {
        this.jobPostings = jobPostings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<ClientJobPosting> getJobPostings() {
        return jobPostings;
    }

    public void addJobPosting(ClientJobPosting jobPosting) {
        jobPostings.add(jobPosting);
        jobPosting.setClientProfile(this);
    }

    public void removeJobPosting(ClientJobPosting jobPosting) {
        jobPostings.remove(jobPosting);
        jobPosting.setClientProfile(null);
    }
}
