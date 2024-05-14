package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.dto.freelancer.FreelancerProfileDTO;
import com.clickcraft.demo.dto.job.JobPostingRequest;
import com.clickcraft.demo.dto.job.JobPostingResponse;
import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.ClientProfile;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.repository.JobPostingRepository;
import com.clickcraft.demo.search.FreelancerProfileSpecifications;
import com.clickcraft.demo.search.JobSpecifications;
import com.clickcraft.demo.service.JobPostingService;
import com.clickcraft.demo.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<JobPostingResponse> getAllJobPostings() {
        List<ClientJobPosting> jobPostings = jobPostingRepository.findAll();
        return jobPostings.stream()
                .map(JobPostingResponse::fromEntity)
                .collect(Collectors.toList());
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

    @Override
    public List<JobPostingResponse> searchJobs(Map<String, String> params) {
        Specification<ClientJobPosting> spec = JobSpecifications.buildSpecification(params);
        List<ClientJobPosting> profiles = jobPostingRepository.findAll(spec);
        return profiles.stream().map(this::convertToJobPostingResponse).collect(Collectors.toList());
    }

    private JobPostingResponse convertToJobPostingResponse(ClientJobPosting clientJobPosting) {
        JobPostingResponse jobPostingResponse = new JobPostingResponse();

        List<String> skillNamesList = clientJobPosting.getRequiredSkills()
                .stream()
                .map(Skill::getSkillName).distinct().collect(Collectors.toList());

        jobPostingResponse.setId(clientJobPosting.getId());
        jobPostingResponse.setJobName(clientJobPosting.getJobName());
        jobPostingResponse.setDescription(clientJobPosting.getDescription());
        jobPostingResponse.setDatePosted(clientJobPosting.getDatePosted());
        jobPostingResponse.setIsRemote(clientJobPosting.getRemote());
        jobPostingResponse.setLocation(clientJobPosting.getLocation());
        jobPostingResponse.setPriceType(clientJobPosting.getPriceType());
        jobPostingResponse.setPriceRangeFrom(clientJobPosting.getPriceRangeFrom());
        jobPostingResponse.setPriceRangeTo(clientJobPosting.getPriceRangeTo());
        jobPostingResponse.setBudget(clientJobPosting.getBudget());
        jobPostingResponse.setJobType(clientJobPosting.getJobType());
        jobPostingResponse.setResumeRequired(clientJobPosting.getResumeRequired());
        jobPostingResponse.setRequiredSkillNames(skillNamesList);
        jobPostingResponse.setFormattedApplicationTime(clientJobPosting.getFormattedDatePosted());
        return jobPostingResponse;
    }

}
