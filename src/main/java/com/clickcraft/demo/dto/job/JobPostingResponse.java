package com.clickcraft.demo.dto.job;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.models.enums.JobType;
import com.clickcraft.demo.models.enums.PriceType;
import com.clickcraft.demo.utils.ApplicationTimeFormatter;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class JobPostingResponse {

    private Long id;
    private String jobName;
    private String description;
    private LocalDateTime datePosted;
    private Boolean isRemote;
    private String location;
    private PriceType priceType;
    private Double priceRangeFrom;
    private Double priceRangeTo;
    private Double budget;
    private JobType jobType;
    private Boolean resumeRequired;
    private List<String> requiredSkillNames;
    private String formattedApplicationTime;
    private int numberOfApplicants;

    public JobPostingResponse() {
    }

    public static JobPostingResponse fromEntity(ClientJobPosting jobPosting) {
        JobPostingResponse response = new JobPostingResponse();
        response.setId(jobPosting.getId());
        response.setJobName(jobPosting.getJobName());
        response.setDescription(jobPosting.getDescription());
        response.setDatePosted(jobPosting.getDatePosted());
        response.setIsRemote(jobPosting.getRemote());
        response.setLocation(jobPosting.getLocation());
        response.setPriceType(jobPosting.getPriceType());
        response.setPriceRangeFrom(jobPosting.getPriceRangeFrom());
        response.setPriceRangeTo(jobPosting.getPriceRangeTo());
        response.setBudget(jobPosting.getBudget());
        response.setJobType(jobPosting.getJobType());
        response.setResumeRequired(jobPosting.getResumeRequired());
        response.setFormattedApplicationTime(ApplicationTimeFormatter.formatApplicationTime(jobPosting.getDatePosted()));

        response.setRequiredSkillNames(jobPosting.getRequiredSkills().stream()
                .map(Skill::getSkillName)
                .collect(Collectors.toList()));

        return response;
    }
}
