package com.clickcraft.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "job_offers")
public class WorkerOfferJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_profile_id")
    private WorkerProfile workerProfile;

    public WorkerProfile getWorkerProfile() {
        return workerProfile;
    }

    public void setWorkerProfile(WorkerProfile workerProfile) {
        this.workerProfile = workerProfile;
    }
}
