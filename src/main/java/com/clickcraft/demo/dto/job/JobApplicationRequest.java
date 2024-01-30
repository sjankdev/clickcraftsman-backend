package com.clickcraft.demo.dto.job;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class JobApplicationRequest {

    @NotBlank
    @Size(max = 1000)
    private String messageToClient;

    public JobApplicationRequest() {
    }

    public String getMessageToClient() {
        return messageToClient;
    }

    public void setMessageToClient(String messageToClient) {
        this.messageToClient = messageToClient;
    }
}