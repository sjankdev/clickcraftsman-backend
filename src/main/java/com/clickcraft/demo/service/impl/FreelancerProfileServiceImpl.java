package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.dto.freelancer.FreelancerProfileDTO;
import com.clickcraft.demo.dto.freelancer.FreelancerProfileUpdateRequest;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.repository.FreelancerProfileRepository;
import com.clickcraft.demo.repository.JobPostingRepository;
import com.clickcraft.demo.repository.SkillRepository;
import com.clickcraft.demo.search.FreelancerProfileSpecifications;
import com.clickcraft.demo.security.repository.UserRepository;
import com.clickcraft.demo.security.services.UserDetailsImpl;
import com.clickcraft.demo.service.FreelancerProfileService;
import com.clickcraft.demo.service.converter.FreelancerProfileConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FreelancerProfileServiceImpl implements FreelancerProfileService {

    private final FreelancerProfileRepository freelancerProfileRepository;
    private final FreelancerProfileConverter freelancerProfileConverter;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final JobPostingRepository jobPostingRepository;

    @Override
    public User getFreelancerByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Freelancer not found with email: " + email));
    }

    @Override
    public void saveFreelancer(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateFreelancerProfile(User user, FreelancerProfileUpdateRequest freelancerProfileUpdateRequest) {
        FreelancerProfile freelancerProfile = user.getFreelancerProfile();
        if (freelancerProfile == null) {
            freelancerProfile = new FreelancerProfile();
            user.setFreelancerProfile(freelancerProfile);
        }

        freelancerProfileConverter.updateFromRequest(freelancerProfile, freelancerProfileUpdateRequest);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public FreelancerProfile getFreelancerProfileByEmail(String email) {
        return freelancerProfileRepository.findByUserEmail(email);
    }

    @Override
    public List<FreelancerProfileDTO> getAllPublicProfiles() {
        List<FreelancerProfile> freelancerProfiles = freelancerProfileRepository.findAll();
        return freelancerProfiles.stream().map(this::convertToFreelancerProfileDTO).collect(Collectors.toList());
    }

    @Override
    public FreelancerProfileDTO getPublicProfileById(Long freelancerId) {
        FreelancerProfile freelancerProfile = freelancerProfileRepository.findById(freelancerId).orElseThrow(() -> new ResourceNotFoundException("Freelancer Profile not found with id: " + freelancerId));
        return convertToFreelancerProfileDTO(freelancerProfile);
    }

    @Override
    public List<String> getProfilePictures(List<Long> freelancerIds) {
        List<String> profilePictures = new ArrayList<>();
        for (Long freelancerId : freelancerIds) {
            User user = userRepository.findById(freelancerId).orElse(null);
            if (user != null) {
                byte[] photoData = user.getProfilePictureData();
                profilePictures.add(photoData != null ? Base64.getEncoder().encodeToString(photoData) : null);
            } else {
                profilePictures.add(null);
            }
        }
        return profilePictures;
    }

    @Override
    public byte[] getProfilePictureData(Long freelancerId) {
        FreelancerProfile freelancerProfile = freelancerProfileRepository.findById(freelancerId).orElseThrow(() -> new ResourceNotFoundException("Freelancer Profile not found with id: " + freelancerId));

        User user = freelancerProfile.getUser();
        return user != null ? user.getProfilePictureData() : null;
    }

    @Override
    public List<FreelancerProfileDTO> searchProfiles(Map<String, String> params) {
        Specification<FreelancerProfile> spec = FreelancerProfileSpecifications.buildSpecification(params);
        List<FreelancerProfile> profiles = freelancerProfileRepository.findAll(spec);
        return profiles.stream().map(this::convertToFreelancerProfileDTO).collect(Collectors.toList());
    }

    @Override
    public List<ClientJobPosting> findMatchingJobs(Long freelancerId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getEmail();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Freelancer not found with email: " + email));

        FreelancerProfile freelancerProfile = user.getFreelancerProfile();
        if (freelancerProfile == null) {
            throw new ResourceNotFoundException("Freelancer Profile not found for user with email: " + email);
        }

        Set<Long> freelancerSkillIds = freelancerProfile.getSkills().stream().map(Skill::getId).collect(Collectors.toSet());

        List<ClientJobPosting> allJobs = jobPostingRepository.findAll();

        return allJobs.stream().filter(job -> job.getRequiredSkills().stream().anyMatch(skill -> freelancerSkillIds.contains(skill.getId()))).collect(Collectors.toList());
    }

    private FreelancerProfileDTO convertToFreelancerProfileDTO(FreelancerProfile freelancerProfile) {
        FreelancerProfileDTO freelancerProfileDTO = new FreelancerProfileDTO();
        freelancerProfileDTO.setId(freelancerProfile.getId());
        freelancerProfileDTO.setFirstName(freelancerProfile.getFirstName());
        freelancerProfileDTO.setLastName(freelancerProfile.getLastName());
        freelancerProfileDTO.setContactPhone(freelancerProfile.getContactPhone());
        freelancerProfileDTO.setLocation(freelancerProfile.getLocation());
        freelancerProfileDTO.setPortfolio(freelancerProfile.getPortfolio());
        freelancerProfileDTO.setYearsOfExperience(freelancerProfile.getYearsOfExperience());
        freelancerProfileDTO.setSkills(freelancerProfile.getSkills().stream().map(Skill::getSkillName).collect(Collectors.toSet()));
        freelancerProfileDTO.setProfilePictureData(freelancerProfile.getUser().getProfilePictureData());
        freelancerProfileDTO.setAboutFreelancer(freelancerProfile.getAboutFreelancer());
        return freelancerProfileDTO;
    }
}