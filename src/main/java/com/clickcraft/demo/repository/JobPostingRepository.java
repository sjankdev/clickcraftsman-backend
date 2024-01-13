package com.clickcraft.demo.repository;

import com.clickcraft.demo.models.ClientJobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<ClientJobPosting, Long> {
}
