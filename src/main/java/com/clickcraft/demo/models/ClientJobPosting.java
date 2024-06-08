package com.clickcraft.demo.models;

import com.clickcraft.demo.models.enums.JobType;
import com.clickcraft.demo.models.enums.PriceType;
import com.clickcraft.demo.utils.ApplicationTimeFormatter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
    @Size(min = 5, max = 100)
    private String jobName;

    @NotBlank
    @Size(min = 10, max = 1000)
    private String description;

    @Column(name = "date_posted")
    private final LocalDateTime datePosted;

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
    @Size(max = 255)
    private String location;

    @Column(name = "is_archived")
    private Boolean archived = false;

    @Column(name = "price_type")
    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    @Column(name = "price_range_from")
    @PositiveOrZero
    private Double priceRangeFrom;

    @Column(name = "price_range_to")
    @PositiveOrZero
    private Double priceRangeTo;

    @Column(name = "budget")
    @PositiveOrZero
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
        this.datePosted = LocalDateTime.now();
        this.clientProfile = null;
    }

    public ClientJobPosting(String jobName, String description, ClientProfile clientProfile, LocalDateTime datePosted, Boolean isRemote, String location, List<Skill> requiredSkills) {
        this.jobName = jobName;
        this.description = description;
        this.clientProfile = clientProfile;
        this.datePosted = datePosted;
        this.remote = isRemote;
        this.location = location;
        this.requiredSkills = requiredSkills;
    }

    public int getNumberOfApplicants() {
        return this.jobApplications.size();
    }

    public int getNumberOfRecentApplicants() {
        LocalDateTime threeHoursAgo = LocalDateTime.now().minusHours(3);
        return (int) this.jobApplications.stream()
                .filter(application -> application.getApplicationTime().isAfter(threeHoursAgo))
                .count();
    }

    public String getFormattedDatePosted() {
        return ApplicationTimeFormatter.formatApplicationTime(this.getDatePosted());
    }

    @AssertTrue(message = "Price range 'from' should be less than or equal to 'to'")
    private boolean isPriceRangeValid() {
        return priceRangeFrom == null || priceRangeTo == null || priceRangeFrom <= priceRangeTo;
    }

    @AssertTrue(message = "Location must be specified if the job is not remote")
    private boolean isLocationValid() {
        return Boolean.TRUE.equals(remote) || (location != null && !location.isBlank());
    }

    public void setPriceRangeFrom(Double priceRangeFrom) {
        if (priceRangeFrom != null && priceRangeFrom < 0) {
            throw new IllegalArgumentException("Price range from cannot be negative");
        }
        this.priceRangeFrom = priceRangeFrom;
    }

    public void setPriceRangeTo(Double priceRangeTo) {
        if (priceRangeTo != null && priceRangeTo < 0) {
            throw new IllegalArgumentException("Price range to cannot be negative");
        }
        if (priceRangeFrom != null && priceRangeTo != null && priceRangeTo < priceRangeFrom) {
            throw new IllegalArgumentException("Price range to must be greater than or equal to price range from");
        }
        this.priceRangeTo = priceRangeTo;
    }
}