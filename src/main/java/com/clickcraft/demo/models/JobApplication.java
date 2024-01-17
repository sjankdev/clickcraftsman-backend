package com.clickcraft.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "job_applications", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"freelancer_profile_id", "client_job_posting_id"})
})
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_profile_id")
    private FreelancerProfile freelancerProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_job_posting_id")
    private ClientJobPosting clientJobPosting;

    public JobApplication() {
    }

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
}
