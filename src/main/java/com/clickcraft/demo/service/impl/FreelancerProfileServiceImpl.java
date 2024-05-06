package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.dto.freelancer.FreelancerProfileDTO;
import com.clickcraft.demo.dto.freelancer.FreelancerProfileUpdateRequest;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.repository.FreelancerProfileRepository;
import com.clickcraft.demo.repository.SkillRepository;
import com.clickcraft.demo.security.repository.UserRepository;
import com.clickcraft.demo.service.FreelancerProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FreelancerProfileServiceImpl implements FreelancerProfileService {

    private static final Logger logger = LoggerFactory.getLogger(FreelancerProfileServiceImpl.class);

    private final FreelancerProfileRepository freelancerProfileRepository;

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @Autowired
    public FreelancerProfileServiceImpl(FreelancerProfileRepository freelancerProfileRepository, UserRepository userRepository, SkillRepository skillRepository) {
        this.freelancerProfileRepository = freelancerProfileRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

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

        freelancerProfile.setFirstName(freelancerProfileUpdateRequest.getFirstName());
        freelancerProfile.setLastName(freelancerProfileUpdateRequest.getLastName());
        freelancerProfile.setContactPhone(freelancerProfileUpdateRequest.getContactPhone());
        freelancerProfile.setLocation(freelancerProfileUpdateRequest.getLocation());
        freelancerProfile.setYearsOfExperience(freelancerProfileUpdateRequest.getYearsOfExperience());
        freelancerProfile.setPortfolio(freelancerProfileUpdateRequest.getPortfolio());
        freelancerProfile.setAboutFreelancer(freelancerProfileUpdateRequest.getAboutFreelancer());

        Set<Skill> skills = freelancerProfileUpdateRequest.getSkills().stream()
                .map(skillName -> skillRepository.findBySkillName(skillName)
                        .orElseGet(() -> {
                            Skill newSkill = new Skill(skillName);
                            skillRepository.save(newSkill);
                            return newSkill;
                        }))
                .collect(Collectors.toSet());

        freelancerProfile.setSkills(skills);
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
        return freelancerProfiles.stream()
                .map(this::convertToFreelancerProfileDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FreelancerProfileDTO getPublicProfileById(Long freelancerId) {
        FreelancerProfile freelancerProfile = freelancerProfileRepository.findById(freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer Profile not found with id: " + freelancerId));
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
        FreelancerProfile freelancerProfile = freelancerProfileRepository.findById(freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer Profile not found with id: " + freelancerId));

        User user = freelancerProfile.getUser();
        return user != null ? user.getProfilePictureData() : null;
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
