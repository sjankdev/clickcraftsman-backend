package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.dto.job.JobPostingRequest;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.repository.JobPostingRepository;
import com.clickcraft.demo.service.JobPostingService;
import com.clickcraft.demo.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final SkillService skillService;

    @Autowired
    public JobPostingServiceImpl(JobPostingRepository jobPostingRepository, SkillService skillService) {
        this.jobPostingRepository = jobPostingRepository;
        this.skillService = skillService;
    }

    @Override
    public void saveJobPosting(ClientJobPosting jobPosting) {
        jobPostingRepository.save(jobPosting);
    }

    @Override
    public List<ClientJobPosting> getAllJobPostings() {
        return jobPostingRepository.findAll();
    }

    @Override
    public List<ClientJobPosting> getClientJobPostings(ClientProfile clientProfile) {
        return jobPostingRepository.findByClientProfile(clientProfile);
    }

    @Override
    public ClientJobPosting createClientJobPosting(JobPostingRequest jobPostingRequest, ClientProfile clientProfile) {
        List<Skill> requiredSkills = skillService.getSkillsByNames(jobPostingRequest.getRequiredSkillIds());

        ClientJobPosting jobPosting = new ClientJobPosting(jobPostingRequest.getJobName(), jobPostingRequest.getDescription(), clientProfile, LocalDateTime.now(), jobPostingRequest.getIsRemote(), jobPostingRequest.getLocation(), requiredSkills);

        jobPosting.setPriceType(jobPostingRequest.getPriceType());
        jobPosting.setPriceRangeFrom(jobPostingRequest.getPriceRangeFrom());
        jobPosting.setPriceRangeTo(jobPostingRequest.getPriceRangeTo());
        jobPosting.setBudget(jobPostingRequest.getBudget());
        jobPosting.setJobType(jobPostingRequest.getJobType());
        jobPosting.setResumeRequired(jobPostingRequest.getResumeRequired());

        clientProfile.addJobPosting(jobPosting);
        return jobPosting;
    }

    @Override
    public int countLiveJobPostingsByClientProfile(ClientProfile clientProfile) {
        List<ClientJobPosting> liveJobPostings = jobPostingRepository.findByClientProfileAndArchived(clientProfile, false);
        return liveJobPostings.size();
    }

    @Override
    public int countArchivedJobPostingsByClientProfile(ClientProfile clientProfile) {
        List<ClientJobPosting> archivedJobPostings = jobPostingRepository.findByClientProfileAndArchived(clientProfile, true);
        return archivedJobPostings.size();
    }

    @Override
    public void deleteJobPosting(Long id) {
        jobPostingRepository.deleteById(id);
    }

}
