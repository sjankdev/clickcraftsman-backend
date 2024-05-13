package com.clickcraft.demo.repository;

import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.enums.ELocations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long> {

    FreelancerProfile findByUserEmail(String email);

    @Query("SELECT fp FROM FreelancerProfile fp JOIN fp.skills s WHERE s.id IN :skillIds")
    List<FreelancerProfile> findBySkillIds(@Param("skillIds") List<Long> skillIds);

    @Query("SELECT fp FROM FreelancerProfile fp WHERE fp.location IN :locations")
    List<FreelancerProfile> findByLocations(@Param("locations") List<ELocations> locations);

    @Query("SELECT fp FROM FreelancerProfile fp JOIN fp.skills s WHERE s.id IN :skillIds AND fp.location IN :locations")
    List<FreelancerProfile> findBySkillIdsAndLocations(@Param("skillIds") List<Long> skillIds, @Param("locations") List<ELocations> locations);

}