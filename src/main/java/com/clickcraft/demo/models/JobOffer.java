package com.clickcraft.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_offers")
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id")
    private FreelancerProfile freelancer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_application_id")
    private JobApplication jobApplication;

    @Column(name = "offer_date")
    private LocalDateTime offerDate;

    @NotBlank
    @Column(name = "message_to_freelancer")
    private String messageToFreelancer;

    public JobOffer() {
        this.offerDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FreelancerProfile getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(FreelancerProfile freelancer) {
        this.freelancer = freelancer;
    }

    public JobApplication getJobApplication() {
        return jobApplication;
    }

    public void setJobApplication(JobApplication jobApplication) {
        this.jobApplication = jobApplication;
    }

    public LocalDateTime getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(LocalDateTime offerDate) {
        this.offerDate = offerDate;
    }

    public String getMessageToFreelancer() {
        return messageToFreelancer;
    }

    public void setMessageToFreelancer(String messageToFreelancer) {
        this.messageToFreelancer = messageToFreelancer;
    }
}
