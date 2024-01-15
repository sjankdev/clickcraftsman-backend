package com.clickcraft.demo.security.repository;

import java.util.Optional;

import com.clickcraft.demo.models.ERole;
import com.clickcraft.demo.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository < Role, Long > {

    Optional < Role > findByName(ERole name);
}