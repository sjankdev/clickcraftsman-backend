package com.clickcraft.demo.security.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/client")
    @PreAuthorize("hasRole('CLIENT') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/freelancer")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String freelancerAccess() {
        return "Freelancer Content.";
    }

    @GetMapping("/all-jobs")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String freelancerAccessAllJobs() {
        return "Freelancer Content. All Jobs";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}