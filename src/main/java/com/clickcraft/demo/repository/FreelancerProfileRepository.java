package com.clickcraft.demo.repository;

import com.clickcraft.demo.models.FreelancerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long> {

    FreelancerProfile findByUserEmail(String email);

    @Query("SELECT fp FROM FreelancerProfile fp JOIN fp.skills s WHERE LOWER(s.skillName) LIKE LOWER(CONCAT('%', :skillName, '%'))")
    List<FreelancerProfile> findBySkillNameContainingIgnoreCase(@Param("skillName") String skillName);

}