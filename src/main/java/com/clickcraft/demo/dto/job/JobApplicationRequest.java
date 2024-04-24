package com.clickcraft.demo.dto.job;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JobApplicationRequest {

    @NotBlank
    @Size(max = 1000)
    private String messageToClient;

    public JobApplicationRequest() {
    }
}