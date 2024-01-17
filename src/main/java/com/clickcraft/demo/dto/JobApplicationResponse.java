package com.clickcraft.demo.dto;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.JobApplication;

public class JobApplicationResponse {
    private Long id;
    private String messageToClient;
    private ClientJobPosting jobPosting;


    public static JobApplicationResponse fromEntity(JobApplication application) {
        JobApplicationResponse response = new JobApplicationResponse();
        response.setId(application.getId());
        response.setMessageToClient(application.getMessageToClient());
        response.setJobPosting(application.getClientJobPosting());
        return response;
    }

    public JobApplicationResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageToClient() {
        return messageToClient;
    }

    public void setMessageToClient(String messageToClient) {
        this.messageToClient = messageToClient;
    }

    public ClientJobPosting getJobPosting() {
        return jobPosting;
    }

    public void setJobPosting(ClientJobPosting jobPosting) {
        this.jobPosting = jobPosting;
    }
}
