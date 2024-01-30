package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.dto.freelancer.FreelancerProfileDTO;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.repository.FreelancerProfileRepository;
import com.clickcraft.demo.security.repository.UserRepository;
import com.clickcraft.demo.service.FreelancerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FreelancerProfileServiceImpl implements FreelancerProfileService {

    @Autowired
    private FreelancerProfileRepository freelancerProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getFreelancerByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Freelancer not found with email: " + email));
    }

    @Override
    public void saveFreelancer(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public FreelancerProfile getFreelancerProfileByEmail(String email) {
        return freelancerProfileRepository.findByUserEmail(email);
    }

    @Override
    public List<FreelancerProfileDTO> getAllPublicProfiles() {
        try {
            List<FreelancerProfile> freelancerProfiles = freelancerProfileRepository.findAll();
            return freelancerProfiles.stream()
                    .map(this::convertToFreelancerProfileDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public FreelancerProfileDTO getPublicProfileById(Long freelancerId) {
        try {
            FreelancerProfile freelancerProfile = freelancerProfileRepository.findById(freelancerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Freelancer Profile not found with id: " + freelancerId));
            return convertToFreelancerProfileDTO(freelancerProfile);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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
        return freelancerProfileDTO;
    }
}
