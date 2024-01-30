package com.clickcraft.demo.service;

import com.clickcraft.demo.dto.FreelancerProfileDTO;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.Skill;
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

    public List<FreelancerProfileDTO> getAllPublicProfiles() {
        try {
            List<FreelancerProfile> freelancerProfiles = freelancerProfileRepository.findAll();
            return freelancerProfiles.stream()
                    .map(this::convertTofreelancerProfileDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public FreelancerProfileDTO getPublicProfileById(Long freelancerId) {
        try {
            FreelancerProfile freelancerProfile = freelancerProfileRepository.findById(freelancerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Freelancer Profile not found with id: " + freelancerId));
            return convertTofreelancerProfileDTO(freelancerProfile);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private FreelancerProfileDTO convertTofreelancerProfileDTO(FreelancerProfile freelancerProfile) {
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
