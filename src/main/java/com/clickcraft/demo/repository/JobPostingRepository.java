package com.clickcraft.demo.repository;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<ClientJobPosting, Long> {

    List<ClientJobPosting> findByClientProfile(ClientProfile clientProfile);

    List<ClientJobPosting> findByClientProfileAndArchived(ClientProfile clientProfile, boolean archived);

}