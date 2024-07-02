package com.clickcraft.demo.service;

import com.clickcraft.demo.dto.freelancer.FreelancerProfileDTO;
import com.clickcraft.demo.dto.freelancer.FreelancerProfileUpdateRequest;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.User;
import com.clickcraft.demo.models.enums.ELocations;

import java.util.List;
import java.util.Map;

public interface FreelancerProfileService {

    User getFreelancerByEmail(String email);

    void saveFreelancer(User user);

    void updateFreelancerProfile(User user, FreelancerProfileUpdateRequest freelancerProfileUpdateRequest);

    FreelancerProfile getFreelancerProfileByEmail(String email);

    List<FreelancerProfileDTO> getAllPublicProfiles();

    FreelancerProfileDTO getPublicProfileById(Long freelancerId);

    List<String> getProfilePictures(List<Long> freelancerIds);

    byte[] getProfilePictureData(Long freelancerId);

    List<FreelancerProfileDTO> searchProfiles(Map<String, String> params);

}
