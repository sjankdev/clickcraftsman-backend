package com.clickcraft.demo.repository;

import com.clickcraft.demo.models.ClientJobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostingRepository extends JpaRepository < ClientJobPosting, Long > {}