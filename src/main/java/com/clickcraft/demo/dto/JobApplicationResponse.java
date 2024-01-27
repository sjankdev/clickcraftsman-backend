package com.clickcraft.demo.dto;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.JobApplication;

public class JobApplicationResponse {
    private Long id;
    private String messageToClient;
    private ClientJobPosting jobPosting;
    private String freelancerFirstName;
    private String freelancerLastName;
    private Long freelancerId;

    public static JobApplicationResponse fromEntity(JobApplication application) {
        JobApplicationResponse response = new JobApplicationResponse();
        response.setId(application.getId());
        response.setMessageToClient(application.getMessageToClient());
        response.setJobPosting(application.getClientJobPosting());

        FreelancerProfile freelancerProfile = application.getFreelancerProfile();
        if (freelancerProfile != null) {
            response.setFreelancerId(freelancerProfile.getId());
            response.setFreelancerFirstName(freelancerProfile.getFirstName());
            response.setFreelancerLastName(freelancerProfile.getLastName());
        }

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

    public Long getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(Long freelancerId) {
        this.freelancerId = freelancerId;
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

    public String getFreelancerFirstName() {
        return freelancerFirstName;
    }

    public void setFreelancerFirstName(String freelancerFirstName) {
        this.freelancerFirstName = freelancerFirstName;
    }

    public String getFreelancerLastName() {
        return freelancerLastName;
    }

    public void setFreelancerLastName(String freelancerLastName) {
        this.freelancerLastName = freelancerLastName;
    }
}
