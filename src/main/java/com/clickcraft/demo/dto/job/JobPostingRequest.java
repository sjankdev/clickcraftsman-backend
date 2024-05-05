package com.clickcraft.demo.dto.job;

import com.clickcraft.demo.models.enums.JobType;
import com.clickcraft.demo.models.enums.PriceType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JobPostingRequest {

    private String jobName;
    private String description;
    private List<String> requiredSkillIds;
    private Boolean isRemote;
    private String location;
    private PriceType priceType;
    private Double priceRangeFrom;
    private Double priceRangeTo;
    private Double budget;
    private JobType jobType;
    private Boolean resumeRequired;

    public JobPostingRequest() {
    }
}