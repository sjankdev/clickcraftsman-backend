package com.clickcraft.demo.service;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.repository.ClientProfileRepository;
import com.clickcraft.demo.repository.FreelancerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FreelancerProfileService {

    @Autowired
    private FreelancerProfileRepository freelancerProfileRepository;

    @Transactional
    public FreelancerProfile getFreelancerProfileByEmail(String email) {
        return freelancerProfileRepository.findByUserEmail(email);
    }


}
