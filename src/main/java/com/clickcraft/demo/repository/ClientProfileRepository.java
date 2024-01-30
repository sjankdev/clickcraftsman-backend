package com.clickcraft.demo.repository;

import com.clickcraft.demo.models.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientProfileRepository extends JpaRepository < ClientProfile, Long > {

    ClientProfile findByUserEmail(String email);

}