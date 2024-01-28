package com.clickcraft.demo.controllers;

import com.clickcraft.demo.dto.UserProfileDTO;
import com.clickcraft.demo.dto.UserProfileUpdateRequest;
import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.payload.response.MessageResponse;
import com.clickcraft.demo.repository.SkillRepository;
import com.clickcraft.demo.security.services.UserDetailsImpl;
import com.clickcraft.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    SkillRepository skillRepository;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
                logger.info("Received a request to fetch user profile. Logged-in User: {} (Email: {})", userDetails.getUsername(), userDetails.getEmail());

                User user = userService.getUserByEmail(userDetails.getEmail());
                UserProfileDTO userProfileDTO = UserProfileDTO.fromUser(user);

                logger.info("User Profile Data: {}", userProfileDTO);
                return ResponseEntity.ok(userProfileDTO);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Error fetching user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<MessageResponse> updateUserProfile(@RequestBody UserProfileUpdateRequest updateRequest) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userService.getUserByEmail(userDetails.getEmail());

            updateProfileData(user, updateRequest);

            userService.saveUser(user);

            logger.info("User profile updated successfully for user: {}", userDetails.getEmail());
            return ResponseEntity.ok(new MessageResponse("User profile updated successfully!"));
        } catch (Exception e) {
            logger.error("Error updating user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void updateProfileData(User user, UserProfileUpdateRequest updateRequest) {
        if (user.getClientProfile() != null) {
            user.getClientProfile().setFirstName(updateRequest.getFirstName());
            user.getClientProfile().setLastName(updateRequest.getLastName());
            user.getClientProfile().setContactPhone(updateRequest.getContactPhone());
            user.getClientProfile().setLocation(updateRequest.getLocation());
        } else if (user.getFreelancerProfile() != null) {
            user.getFreelancerProfile().setFirstName(updateRequest.getFirstName());
            user.getFreelancerProfile().setLastName(updateRequest.getLastName());
            user.getFreelancerProfile().setContactPhone(updateRequest.getContactPhone());
            user.getFreelancerProfile().setLocation(updateRequest.getLocation());
            user.getFreelancerProfile().setYearsOfExperience(updateRequest.getYearsOfExperience());
            user.getFreelancerProfile().setPortfolio(updateRequest.getPortfolio());
            Set<Skill> skills = updateRequest.getSkills().stream()
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
}
