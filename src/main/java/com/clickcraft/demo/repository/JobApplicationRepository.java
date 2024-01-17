package com.clickcraft.demo.repository;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByFreelancerProfileAndClientJobPosting(FreelancerProfile freelancerProfile, ClientJobPosting clientJobPosting);

    @Query("SELECT DISTINCT j.clientJobPosting.id FROM JobApplication j WHERE j.freelancerProfile = :freelancerProfile")
    List<Long> findAppliedJobIdsByFreelancerProfile(FreelancerProfile freelancerProfile);
}

