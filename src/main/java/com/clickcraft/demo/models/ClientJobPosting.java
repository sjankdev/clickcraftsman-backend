package com.clickcraft.demo.models;

import com.clickcraft.demo.models.enums.JobType;
import com.clickcraft.demo.models.enums.PriceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "client_job_postings")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    private List<Skill> requiredSkills;

    @Column(name = "is_remote")
    @JsonProperty("isRemote")
    private Boolean remote;

    @Column(name = "location")
    private String location;

    @Column(name = "is_archived")
    private Boolean archived = false;

    @Column(name = "price_type")
    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    @Column(name = "price_range_from")
    private Double priceRangeFrom;

    @Column(name = "price_range_to")
    private Double priceRangeTo;

    @Column(name = "budget")
    private Double budget;

    @Column(name = "job_type")
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Column(name = "resume_required")
    private Boolean resumeRequired;

    @OneToMany(mappedBy = "clientJobPosting", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<JobApplication> jobApplications = new HashSet<>();

    public ClientJobPosting() {
        this.datePosted = LocalDate.now();
        this.clientProfile = null;
    }

    public ClientJobPosting(String jobName, String description, ClientProfile clientProfile, LocalDate datePosted, Boolean isRemote, String location, List<Skill> requiredSkills) {
        this.jobName = jobName;
        this.description = description;
        this.clientProfile = clientProfile;
        this.datePosted = datePosted;
        this.remote = isRemote;
        this.location = location;
        this.requiredSkills = requiredSkills;
    }
}