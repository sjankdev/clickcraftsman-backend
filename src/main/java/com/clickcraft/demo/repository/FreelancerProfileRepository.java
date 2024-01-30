package com.clickcraft.demo.repository;

import com.clickcraft.demo.models.FreelancerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreelancerProfileRepository extends JpaRepository < FreelancerProfile, Long > {

    FreelancerProfile findByUserEmail(String email);

}