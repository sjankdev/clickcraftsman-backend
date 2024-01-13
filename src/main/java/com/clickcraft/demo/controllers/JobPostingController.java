package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.JobPostingRequest;
import com.clickcraft.demo.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/job-postings")
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

    @PostMapping("/{clientId}")
    public ResponseEntity<?> postJob(@PathVariable Long clientId, @RequestBody JobPostingRequest jobPostingRequest) {
        jobPostingService.postJob(clientId, jobPostingRequest);
        return ResponseEntity.ok("Job posted successfully");
    }
}
