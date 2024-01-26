package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.PublicProfileDTO;
import com.clickcraft.demo.service.FreelancerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/public-freelancers")
public class FreelancerController {

    @Autowired
    private FreelancerProfileService freelancerProfileService;

    @GetMapping("/getAllFreelancers")
    public ResponseEntity<List<PublicProfileDTO>> getAllPublicProfiles() {
        try {
            List<PublicProfileDTO> publicProfiles = freelancerProfileService.getAllPublicProfiles();
            return new ResponseEntity<>(publicProfiles, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{freelancerId}")
    public ResponseEntity<PublicProfileDTO> getPublicProfileById(@PathVariable String freelancerId) {
        try {
            // Check if freelancerId is a valid Long
            Long id = Long.valueOf(freelancerId);

            PublicProfileDTO publicProfile = freelancerProfileService.getPublicProfileById(id);
            return new ResponseEntity<>(publicProfile, HttpStatus.OK);
        } catch (NumberFormatException e) {
            // Log the exception for debugging
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}