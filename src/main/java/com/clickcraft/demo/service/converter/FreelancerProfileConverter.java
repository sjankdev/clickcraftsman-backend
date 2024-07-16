package com.clickcraft.demo.service.converter;

import com.clickcraft.demo.dto.freelancer.FreelancerProfileDTO;
import com.clickcraft.demo.dto.freelancer.FreelancerProfileUpdateRequest;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.repository.SkillRepository;
import com.clickcraft.demo.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FreelancerProfileConverter {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public FreelancerProfile updateFromRequest(FreelancerProfile freelancerProfile, FreelancerProfileUpdateRequest request) {
        freelancerProfile.setFirstName(request.getFirstName());
        freelancerProfile.setLastName(request.getLastName());
        freelancerProfile.setContactPhone(request.getContactPhone());
        freelancerProfile.setLocation(request.getLocation());
        freelancerProfile.setYearsOfExperience(request.getYearsOfExperience());
        freelancerProfile.setPortfolio(request.getPortfolio());
        freelancerProfile.setAboutFreelancer(request.getAboutFreelancer());

        Set<Skill> skills = request.getSkills().stream()
                .map(skillName -> skillRepository.findBySkillName(skillName)
                        .orElseGet(() -> {
                            Skill newSkill = new Skill(skillName);
                            skillRepository.save(newSkill);
                            return newSkill;
                        }))
                .collect(Collectors.toSet());

        freelancerProfile.setSkills(skills);

        return freelancerProfile;
    }

    public FreelancerProfileDTO toDTO(FreelancerProfile freelancerProfile) {
        FreelancerProfileDTO dto = new FreelancerProfileDTO();
        dto.setId(freelancerProfile.getId());
        dto.setFirstName(freelancerProfile.getFirstName());
        dto.setLastName(freelancerProfile.getLastName());
        dto.setContactPhone(freelancerProfile.getContactPhone());
        dto.setLocation(freelancerProfile.getLocation());
        dto.setPortfolio(freelancerProfile.getPortfolio());
        dto.setYearsOfExperience(freelancerProfile.getYearsOfExperience());
        dto.setSkills(freelancerProfile.getSkills().stream().map(Skill::getSkillName).collect(Collectors.toSet()));
        dto.setProfilePictureData(freelancerProfile.getUser().getProfilePictureData());
        dto.setAboutFreelancer(freelancerProfile.getAboutFreelancer());
        return dto;
    }
}
