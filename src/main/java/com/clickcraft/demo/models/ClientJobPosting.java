package com.clickcraft.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "client_job_postings")
@JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
})
public class ClientJobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String jobName;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @Column(name = "date_posted")
    private final LocalDate datePosted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_profile_id")
    @JsonIgnore
    private final ClientProfile clientProfile;

    @ManyToMany
    @JoinTable(name = "job_posting_skills", joinColumns = @JoinColumn(name = "job_posting_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List < Skill > requiredSkills;

    @Column(name = "is_remote")
    @JsonProperty("isRemote")
    private Boolean remote;

    @Column(name = "location")
    private String location;

    @OneToMany(mappedBy = "clientJobPosting", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set < JobApplication > jobApplications = new HashSet < > ();

    public ClientJobPosting() {
        this.datePosted = LocalDate.now();
        this.clientProfile = null;
    }

    public ClientJobPosting(String jobName, String description, ClientProfile clientProfile,
                            LocalDate datePosted, Boolean isRemote, String location, List < Skill > requiredSkills) {
        this.jobName = jobName;
        this.description = description;
        this.clientProfile = clientProfile;
        this.datePosted = datePosted;
        this.remote = isRemote;
        this.location = location;
        this.requiredSkills = requiredSkills;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDatePosted() {
        return datePosted;
    }

    public ClientProfile getClientProfile() {
        return clientProfile;
    }

    public List < Skill > getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List < Skill > requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public Boolean getRemote() {
        return remote;
    }

    public void setRemote(Boolean remote) {
        this.remote = remote;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set < JobApplication > getJobApplications() {
        return jobApplications;
    }

    public void setJobApplications(Set < JobApplication > jobApplications) {
        this.jobApplications = jobApplications;
    }
}