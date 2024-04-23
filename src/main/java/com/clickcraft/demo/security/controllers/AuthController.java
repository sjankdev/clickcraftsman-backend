package com.clickcraft.demo.security.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.clickcraft.demo.models.*;
import com.clickcraft.demo.models.enums.ERole;
import com.clickcraft.demo.repository.PhotoRepository;
import com.clickcraft.demo.security.payload.request.LoginRequest;
import com.clickcraft.demo.security.payload.request.SignupRequest;
import com.clickcraft.demo.security.payload.response.JwtResponse;
import com.clickcraft.demo.security.payload.response.MessageResponse;
import com.clickcraft.demo.security.repository.RoleRepository;
import com.clickcraft.demo.repository.SkillRepository;
import com.clickcraft.demo.security.repository.UserRepository;
import com.clickcraft.demo.security.jwt.JwtUtils;
import com.clickcraft.demo.security.services.UserDetailsImpl;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final SkillRepository skillRepository;
    private final PhotoRepository photoRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, SkillRepository skillRepository, PhotoRepository photoRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.skillRepository = skillRepository;
        this.photoRepository = photoRepository;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getEmail(), roles));
    }

    @PostMapping(value = "/signup", consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<?> registerUser(@RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture, @RequestPart("signUpRequest") SignupRequest signUpRequest) {
        try {
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
            }

            logger.info("Creating user entity...");
            User user = new User(signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));

            Set<String> strRoles = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role userRole = roleRepository.findByName(ERole.ROLE_CLIENT).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);
                            break;
                        case "mod":
                            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(modRole);
                            break;
                        case "freelancer":
                            Role freelancerRole = roleRepository.findByName(ERole.ROLE_FREELANCER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(freelancerRole);
                            break;
                        default:
                            Role clientRole = roleRepository.findByName(ERole.ROLE_CLIENT).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(clientRole);
                    }
                });
            }

            user.setRoles(roles);

            if (strRoles != null && strRoles.contains("freelancer")) {
                FreelancerProfile freelancerProfile = FreelancerProfile.createFromSignupRequestFreelancer(signUpRequest, user);

                Set<String> selectedSkills = signUpRequest.getSkills();
                if (selectedSkills != null && !selectedSkills.isEmpty()) {
                    for (String selectedSkillName : selectedSkills) {
                        Skill skill = skillRepository.findBySkillName(selectedSkillName).orElseGet(() -> {
                            Skill newSkill = new Skill();
                            newSkill.setSkillName(selectedSkillName);
                            return skillRepository.save(newSkill);
                        });

                        freelancerProfile.getSkills().add(skill);
                    }
                }
                user.setFreelancerProfile(freelancerProfile);
            } else {
                ClientProfile clientProfile = ClientProfile.createFromSignupRequestClient(signUpRequest, user);
                user.setClientProfile(clientProfile);
            }

            if (profilePicture != null) {
                logger.info("Received profile picture: {}", profilePicture.getOriginalFilename());

                Photo photo = new Photo();
                photo.setData(profilePicture.getBytes());

                user = userRepository.save(user);
                photo.setUser(user);

                photo = photoRepository.save(photo);
                user.setProfilePictureId(photo.getId());
            } else {
                logger.info("No profile picture received");
            }

            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal Server Error"));
        }
    }
}
