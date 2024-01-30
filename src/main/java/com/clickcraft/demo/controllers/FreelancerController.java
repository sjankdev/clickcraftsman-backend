package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.FreelancerProfileDTO;
import com.clickcraft.demo.service.FreelancerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/freelancer")
public class FreelancerController {

    @Autowired
    private FreelancerProfileService freelancerProfileService;

    @GetMapping("/getAllFreelancers")
    public ResponseEntity<List<FreelancerProfileDTO>> getAllPublicProfiles() {
        try {
            List<FreelancerProfileDTO> publicProfiles = freelancerProfileService.getAllPublicProfiles();
            return new ResponseEntity<>(publicProfiles, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{freelancerId}")
    public ResponseEntity<FreelancerProfileDTO> getPublicProfileById(@PathVariable String freelancerId) {
        try {
            Long id = Long.valueOf(freelancerId);

            FreelancerProfileDTO publicProfile = freelancerProfileService.getPublicProfileById(id);
            return new ResponseEntity<>(publicProfile, HttpStatus.OK);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}