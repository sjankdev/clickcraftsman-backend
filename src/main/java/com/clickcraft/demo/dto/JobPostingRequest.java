package com.clickcraft.demo.dto;


import java.util.List;

public class JobPostingRequest {
    private String jobName;
    private String description;
    private List<String> requiredSkillIds;

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

    public List<String> getRequiredSkillIds() {
        return requiredSkillIds;
    }

    public void setRequiredSkillIds(List<String> requiredSkillIds) {
        this.requiredSkillIds = requiredSkillIds;
    }
}
