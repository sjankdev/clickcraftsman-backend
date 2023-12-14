package com.clickcraft.demo.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.clickcraft.demo.models.*;
import com.clickcraft.demo.payload.request.LoginRequest;
import com.clickcraft.demo.payload.request.SignupRequest;
import com.clickcraft.demo.payload.response.JwtResponse;
import com.clickcraft.demo.payload.response.MessageResponse;
import com.clickcraft.demo.repository.RoleRepository;
import com.clickcraft.demo.repository.UserRepository;
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

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            logger.info("Received registration request for username: {}", signUpRequest.getUsername());

            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
            }

            User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));

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
                        case "worker":
                            Role workerRole = roleRepository.findByName(ERole.ROLE_WORKER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(workerRole);
                            break;
                        default:
                            Role clientRole = roleRepository.findByName(ERole.ROLE_CLIENT).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(clientRole);
                    }
                });
            }

            user.setRoles(roles);

            if (strRoles != null && strRoles.contains("worker")) {
                WorkerProfile workerProfile = new WorkerProfile();
                workerProfile.setFirstName(signUpRequest.getFirstName());
                workerProfile.setLastName(signUpRequest.getLastName());
                workerProfile.setContactPhone(signUpRequest.getContactPhone());
                workerProfile.setLocation(signUpRequest.getLocation());
                workerProfile.setSkills(signUpRequest.getSkills());
                workerProfile.setPortfolio(signUpRequest.getPortfolio());
                workerProfile.setYearsOfExperience(signUpRequest.getYearsOfExperience());
                workerProfile.setUser(user);
                user.setWorkerProfile(workerProfile);
            } else {
                ClientProfile clientProfile = new ClientProfile();
                clientProfile.setFirstName(signUpRequest.getFirstName());
                clientProfile.setLastName(signUpRequest.getLastName());
                clientProfile.setContactPhone(signUpRequest.getContactPhone());
                clientProfile.setLocation(signUpRequest.getLocation());
                clientProfile.setUser(user);
                user.setClientProfile(clientProfile);
            }

            userRepository.save(user);

            logger.info("User registered successfully: {}", user.getUsername());

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal Server Error"));
        }
    }

    }
