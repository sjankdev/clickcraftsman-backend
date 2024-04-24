package com.clickcraft.demo.dto.job;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JobApplicationRequest {

    @NotBlank
    @Size(max = 1000)
    private String messageToClient;

    @NotNull
    @PositiveOrZero
    private Double desiredPay;

    public JobApplicationRequest() {
    }
}