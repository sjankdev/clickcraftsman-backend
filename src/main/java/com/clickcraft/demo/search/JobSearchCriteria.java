package com.clickcraft.demo.search;

import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Accessors(chain = true)
public class JobSearchCriteria {
    private List<String> locations;
    private List<String> skillIds;
    private List<String> jobTypes;
    private List<String> priceTypes;
    private Double priceRangeFrom;
    private Double priceRangeTo;
    private Double budgetFrom;
    private Double budgetTo;
    private String jobName;
    private Boolean isRemote;
    private Boolean resumeRequired;
    private String dateRange;

    public Map<String, String> toMap() {
        Map<String, String> params = new HashMap<>();
        if (locations != null && !locations.isEmpty()) {
            params.put("locations", String.join(",", locations));
        }
        if (skillIds != null && !skillIds.isEmpty()) {
            params.put("skillIds", String.join(",", skillIds));
        }
        if (jobTypes != null && !jobTypes.isEmpty()) {
            List<String> uppercaseJobTypes = jobTypes.stream().map(String::toUpperCase).collect(Collectors.toList());
            params.put("jobTypes", String.join(",", uppercaseJobTypes));
        }
        if (priceTypes != null && !priceTypes.isEmpty()) {
            List<String> uppercasePriceTypes = priceTypes.stream().map(String::toUpperCase).collect(Collectors.toList());
            params.put("priceTypes", String.join(",", uppercasePriceTypes));
        }
        if (priceRangeFrom != null) {
            params.put("priceRangeFrom", String.valueOf(priceRangeFrom));
        }
        if (priceRangeTo != null) {
            params.put("priceRangeTo", String.valueOf(priceRangeTo));
        }
        if (budgetFrom != null) {
            params.put("budgetFrom", String.valueOf(budgetFrom));
        }
        if (budgetTo != null) {
            params.put("budgetTo", String.valueOf(budgetTo));
        }
        if (jobName != null && !jobName.isEmpty()) {
            params.put("jobName", jobName);
        }
        if (isRemote != null) {
            params.put("isRemote", String.valueOf(isRemote));
        }
        if (resumeRequired != null) {
            params.put("resumeRequired", String.valueOf(resumeRequired));
        }
        if (dateRange != null && !dateRange.isEmpty()) {
            params.put("dateRange", dateRange);
        }
        return params;
    }
}
