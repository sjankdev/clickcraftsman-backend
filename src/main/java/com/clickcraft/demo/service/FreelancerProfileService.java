package com.clickcraft.demo.service;

import com.clickcraft.demo.dto.freelancer.FreelancerProfileDTO;
import com.clickcraft.demo.dto.freelancer.FreelancerProfileUpdateRequest;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.models.enums.ELocations;

import java.util.List;

public interface FreelancerProfileService {

    User getFreelancerByEmail(String email);

    void saveFreelancer(User user);

    void updateFreelancerProfile(User user, FreelancerProfileUpdateRequest freelancerProfileUpdateRequest);

    FreelancerProfile getFreelancerProfileByEmail(String email);

    List<FreelancerProfileDTO> getAllPublicProfiles();

    FreelancerProfileDTO getPublicProfileById(Long freelancerId);

    List<String> getProfilePictures(List<Long> freelancerIds);

    byte[] getProfilePictureData(Long freelancerId);

    List<FreelancerProfileDTO> searchBySkillIds(List<Long> skillIds);

    List<FreelancerProfileDTO> searchByLocations(List<ELocations> locations);

    List<FreelancerProfileDTO> searchBySkillAndLocation(List<Long> skillIds, List<ELocations> locations);

}
