package com.clickcraft.demo.dto.job;

import com.clickcraft.demo.models.enums.JobType;
import com.clickcraft.demo.models.enums.PriceType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JobPostingRequest {

    @NotBlank
    @Size(min = 5, max = 100)
    private String jobName;

    @NotBlank
    @Size(min = 10, max = 1000)
    private String description;

    private List<String> requiredSkillIds;

    private Boolean isRemote;

    @Size(max = 255)
    private String location;

    private PriceType priceType;

    @PositiveOrZero
    private Double priceRangeFrom;

    @PositiveOrZero
    private Double priceRangeTo;

    @PositiveOrZero
    private Double budget;

    private JobType jobType;

    private Boolean resumeRequired;

    public JobPostingRequest() {
    }

    @AssertTrue(message = "Price range 'from' should be less than or equal to 'to'")
    private boolean isPriceRangeValid() {
        return priceRangeFrom == null || priceRangeTo == null || priceRangeFrom <= priceRangeTo;
    }

    @AssertTrue(message = "Location must be specified if the job is not remote")
    private boolean isLocationValid() {
        return Boolean.TRUE.equals(isRemote) || (location != null && !location.isBlank());
    }

    public void setPriceRangeFrom(Double priceRangeFrom) {
        if (priceRangeFrom != null && priceRangeFrom < 0) {
            throw new IllegalArgumentException("Price range from cannot be negative");
        }
        this.priceRangeFrom = priceRangeFrom;
    }

    public void setPriceRangeTo(Double priceRangeTo) {
        if (priceRangeTo != null && priceRangeTo < 0) {
            throw new IllegalArgumentException("Price range to cannot be negative");
        }
        if (priceRangeFrom != null && priceRangeTo != null && priceRangeTo < priceRangeFrom) {
            throw new IllegalArgumentException("Price range to must be greater than or equal to price range from");
        }
        this.priceRangeTo = priceRangeTo;
    }
}
