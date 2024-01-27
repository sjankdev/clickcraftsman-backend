package com.clickcraft.demo.service;

import com.clickcraft.demo.dto.PublicProfileDTO;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.repository.ClientProfileRepository;
import com.clickcraft.demo.repository.FreelancerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FreelancerProfileService {

    @Autowired
    private FreelancerProfileRepository freelancerProfileRepository;

    @Transactional
    public FreelancerProfile getFreelancerProfileByEmail(String email) {
        return freelancerProfileRepository.findByUserEmail(email);
    }

    public List<PublicProfileDTO> getAllPublicProfiles() {
        try {
            List<FreelancerProfile> freelancerProfiles = freelancerProfileRepository.findAll();
            return freelancerProfiles.stream()
                    .map(this::convertToPublicProfileDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            throw e; // rethrow the exception
        }
    }

    public PublicProfileDTO getPublicProfileById(Long freelancerId) {
        try {
            FreelancerProfile freelancerProfile = freelancerProfileRepository.findById(freelancerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Freelancer Profile not found with id: " + freelancerId));
            return convertToPublicProfileDTO(freelancerProfile);
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            throw e; // rethrow the exception
        }
    }

    private PublicProfileDTO convertToPublicProfileDTO(FreelancerProfile freelancerProfile) {
        PublicProfileDTO publicProfileDTO = new PublicProfileDTO();
        publicProfileDTO.setId(freelancerProfile.getId());
        publicProfileDTO.setFirstName(freelancerProfile.getFirstName());
        publicProfileDTO.setLastName(freelancerProfile.getLastName());
        publicProfileDTO.setContactPhone(freelancerProfile.getContactPhone());
        publicProfileDTO.setLocation(freelancerProfile.getLocation());
        publicProfileDTO.setPortfolio(freelancerProfile.getPortfolio());
        publicProfileDTO.setYearsOfExperience(freelancerProfile.getYearsOfExperience());
        publicProfileDTO.setSkills(freelancerProfile.getSkills().stream().map(Skill::getSkillName).collect(Collectors.toSet()));
        return publicProfileDTO;
    }

}
