package com.clickcraft.demo.service;

import com.clickcraft.demo.dto.freelancer.FreelancerProfileDTO;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.User;

import java.util.List;

public interface FreelancerProfileService {

    User getFreelancerByEmail(String email);

    void saveFreelancer(User user);

    FreelancerProfile getFreelancerProfileByEmail(String email);

    List<FreelancerProfileDTO> getAllPublicProfiles();

    FreelancerProfileDTO getPublicProfileById(Long freelancerId);

    public List<String> getProfilePictures(List<Long> freelancerIds);

    byte[] getProfilePictureData(Long freelancerId);
}
