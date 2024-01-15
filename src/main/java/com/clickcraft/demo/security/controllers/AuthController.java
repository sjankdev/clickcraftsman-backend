package com.clickcraft.demo.security.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.clickcraft.demo.models.*;
import com.clickcraft.demo.payload.request.LoginRequest;
import com.clickcraft.demo.payload.request.SignupRequest;
import com.clickcraft.demo.payload.response.JwtResponse;
import com.clickcraft.demo.payload.response.MessageResponse;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    SkillRepository skillRepository;

    @PostMapping("/signin")
    public ResponseEntity < ? > authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List < String > roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getEmail(), roles));
    }

    @Transactional
    @PostMapping("/signup")
    public ResponseEntity < ? > registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
            }

            User user = new User(signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));

            Set < String > strRoles = signUpRequest.getRole();
            Set < Role > roles = new HashSet < > ();

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

                Set < String > selectedSkills = signUpRequest.getSkills();
                if (selectedSkills != null && !selectedSkills.isEmpty()) {
                    for (String selectedSkillName: selectedSkills) {
                        Skill skill = skillRepository.findBySkillName(selectedSkillName)
                                .orElseGet(() -> {
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

            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal Server Error"));
        }
    }

}