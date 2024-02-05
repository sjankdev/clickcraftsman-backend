package com.clickcraft.demo.repository;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository < JobApplication, Long > {

    boolean existsByFreelancerProfileAndClientJobPosting(FreelancerProfile freelancerProfile, ClientJobPosting clientJobPosting);

    @Query("SELECT DISTINCT j.clientJobPosting.id FROM JobApplication j WHERE j.freelancerProfile = :freelancerProfile")
    List < Long > findAppliedJobIdsByFreelancerProfile(FreelancerProfile freelancerProfile);

    @Query("SELECT ja FROM JobApplication ja " +
            "JOIN ja.clientJobPosting jp " +
            "WHERE jp.clientProfile = :clientProfile")
    List < JobApplication > findClientJobApplications(ClientProfile clientProfile);

    @Query("SELECT ja FROM JobApplication ja " +
            "JOIN ja.clientJobPosting jp " +
            "WHERE jp.id = :jobId")
    List < JobApplication > findJobApplicationsByJobId(@Param("jobId") Long jobId);

    List<JobApplication> findByFreelancerProfile(FreelancerProfile freelancerProfile);

}