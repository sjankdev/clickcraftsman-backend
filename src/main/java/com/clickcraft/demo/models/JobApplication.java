package com.clickcraft.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "job_applications", uniqueConstraints = {@UniqueConstraint(columnNames = {"freelancer_profile_id", "client_job_posting_id"})})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "freelancerProfile"})

public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 1000)
    private String messageToClient;

    @NotNull
    @PositiveOrZero
    private Double desiredPay;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(length = 100000, name = "resume", nullable = true)
    private byte[] resume;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "application_time")
    private LocalDateTime applicationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_profile_id")
    @JsonIgnore
    private FreelancerProfile freelancerProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_job_posting_id")
    private ClientJobPosting clientJobPosting;

    public JobApplication() {
        this.applicationTime = LocalDateTime.now();
    }
}