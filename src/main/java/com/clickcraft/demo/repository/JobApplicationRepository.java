package com.clickcraft.demo.repository;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByFreelancerProfileAndClientJobPosting(FreelancerProfile freelancerProfile, ClientJobPosting clientJobPosting);
}

