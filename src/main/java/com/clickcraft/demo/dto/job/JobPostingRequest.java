package com.clickcraft.demo.dto.job;

import java.util.List;

public class JobPostingRequest {
    private String jobName;
    private String description;
    private List < String > requiredSkillIds;
    private Boolean isRemote;
    private String location;

    public JobPostingRequest() {
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List < String > getRequiredSkillIds() {
        return requiredSkillIds;
    }

    public void setRequiredSkillIds(List < String > requiredSkillIds) {
        this.requiredSkillIds = requiredSkillIds;
    }

    public Boolean getIsRemote() {
        return isRemote;
    }

    public void setIsRemote(Boolean isRemote) {
        this.isRemote = isRemote;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}