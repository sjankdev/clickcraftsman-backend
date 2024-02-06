package com.clickcraft.demo.models;

import com.clickcraft.demo.models.enums.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "job_applications", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "freelancer_profile_id",
                "client_job_posting_id"
        })
})
@JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler",
        "freelancerProfile"
})

public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 1000)
    private String messageToClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_profile_id")
    @JsonIgnore
    private FreelancerProfile freelancerProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_job_posting_id")
    private ClientJobPosting clientJobPosting;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @OneToOne(mappedBy = "jobApplication", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private JobOffer jobOffer;

    public JobApplication() {}

    public FreelancerProfile getFreelancerProfile() {
        return freelancerProfile;
    }

    public void setFreelancerProfile(FreelancerProfile freelancerProfile) {
        this.freelancerProfile = freelancerProfile;
    }

    public ClientJobPosting getClientJobPosting() {
        return clientJobPosting;
    }

    public void setClientJobPosting(ClientJobPosting clientJobPosting) {
        this.clientJobPosting = clientJobPosting;
    }

    public String getMessageToClient() {
        return messageToClient;
    }

    public void setMessageToClient(String messageToClient) {
        this.messageToClient = messageToClient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public JobOffer getJobOffer() {
        return jobOffer;
    }

    public void setJobOffer(JobOffer jobOffer) {
        this.jobOffer = jobOffer;
    }
}