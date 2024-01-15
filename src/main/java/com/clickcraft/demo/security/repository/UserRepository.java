package com.clickcraft.demo.security.repository;

import java.util.Optional;

import com.clickcraft.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository < User, Long > {

    Optional < User > findByEmail(String email);

    Boolean existsByEmail(String email);
}