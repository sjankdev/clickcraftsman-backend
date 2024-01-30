package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.freelancer.FreelancerProfileDTO;
import com.clickcraft.demo.dto.freelancer.FreelancerProfileUpdateRequest;
import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.repository.SkillRepository;
import com.clickcraft.demo.security.payload.response.MessageResponse;
import com.clickcraft.demo.security.services.UserDetailsImpl;
import com.clickcraft.demo.service.FreelancerProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/freelancer")
public class FreelancerController {

    private static final Logger logger = LoggerFactory.getLogger(FreelancerController.class);

    @Autowired
    private FreelancerProfileService freelancerProfileService;

    @Autowired
    SkillRepository skillRepository;

    @PostMapping("/update")
    public ResponseEntity<MessageResponse> updateClientProfile(@RequestBody FreelancerProfileUpdateRequest freelancerProfileUpdateRequest) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = freelancerProfileService.getFreelancerByEmail(userDetails.getEmail());

            updateFreelancerProfileData(user, freelancerProfileUpdateRequest);

            freelancerProfileService.saveFreelancer(user);

            logger.info("User profile updated successfully for user: {}", userDetails.getEmail());
            return ResponseEntity.ok(new MessageResponse("User profile updated successfully!"));
        } catch (Exception e) {
            logger.error("Error updating user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void updateFreelancerProfileData(User user, FreelancerProfileUpdateRequest freelancerProfileUpdateRequest) {
        if (user.getFreelancerProfile() != null) {
            user.getFreelancerProfile().setFirstName(freelancerProfileUpdateRequest.getFirstName());
            user.getFreelancerProfile().setLastName(freelancerProfileUpdateRequest.getLastName());
            user.getFreelancerProfile().setContactPhone(freelancerProfileUpdateRequest.getContactPhone());
            user.getFreelancerProfile().setLocation(freelancerProfileUpdateRequest.getLocation());
            user.getFreelancerProfile().setLocation(freelancerProfileUpdateRequest.getLocation());
            user.getFreelancerProfile().setYearsOfExperience(freelancerProfileUpdateRequest.getYearsOfExperience());
            user.getFreelancerProfile().setPortfolio(freelancerProfileUpdateRequest.getPortfolio());
            Set<Skill> skills = freelancerProfileUpdateRequest.getSkills().stream()
                    .map(skillName -> {
                        Skill skill = skillRepository.findBySkillName(skillName)
                                .orElseGet(() -> {
                                    Skill newSkill = new Skill(skillName);
                                    skillRepository.save(newSkill);
                                    return newSkill;
                                });
                        return skill;
                    })
                    .collect(Collectors.toSet());

            user.getFreelancerProfile().setSkills(skills);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<FreelancerProfileDTO> getFreelancerProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
                logger.info("Received a request to fetch freelancer profile. Logged-in Freelancer: {} (Email: {})", userDetails.getUsername(), userDetails.getEmail());

                User user = freelancerProfileService.getFreelancerByEmail(userDetails.getEmail());
                FreelancerProfileDTO freelancerProfileDTO = FreelancerProfileDTO.fromUser(user);

                logger.info("Freelancer Profile Data: {}", freelancerProfileDTO);
                return ResponseEntity.ok(freelancerProfileDTO);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Error fetching Freelancer profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

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