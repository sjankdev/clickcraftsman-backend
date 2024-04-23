package com.clickcraft.demo.dto.job;

import java.util.List;

public class JobPostingRequest {
    private String jobName;
    private String description;
    private List < String > requiredSkillIds;
    private Boolean isRemote;
    private String location;
    private String priceType;
    private Double priceRangeFrom;
    private Double priceRangeTo;
    private Double budget;

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

    public Boolean getRemote() {
        return isRemote;
    }

    public void setRemote(Boolean remote) {
        isRemote = remote;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public Double getPriceRangeFrom() {
        return priceRangeFrom;
    }

    public void setPriceRangeFrom(Double priceRangeFrom) {
        this.priceRangeFrom = priceRangeFrom;
    }

    public Double getPriceRangeTo() {
        return priceRangeTo;
    }

    public void setPriceRangeTo(Double priceRangeTo) {
        this.priceRangeTo = priceRangeTo;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }
}