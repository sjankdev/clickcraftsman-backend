package com.clickcraft.demo.dto.job;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.JobApplication;
import com.clickcraft.demo.utils.ApplicationTimeFormatter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JobApplicationResponse {

    private Long id;
    private String messageToClient;
    private ClientJobPosting jobPosting;
    private String freelancerFirstName;
    private String freelancerLastName;
    private Long freelancerId;
    private Double desiredPay;
    private String freelancerEmail;
    private byte[] resume;
    private boolean hasResume;
    private String formattedApplicationTime;

    public JobApplicationResponse() {
    }

    public static JobApplicationResponse fromEntity(JobApplication application) {
        JobApplicationResponse response = new JobApplicationResponse();
        response.setId(application.getId());
        response.setMessageToClient(application.getMessageToClient());
        response.setDesiredPay(application.getDesiredPay());
        response.setJobPosting(application.getClientJobPosting());
        response.setFormattedApplicationTime(ApplicationTimeFormatter.formatApplicationTime(application.getApplicationTime()));

        if (application.getResume() != null) {
            response.setResume(application.getResume());
            response.setHasResume(true);
        } else {
            response.setHasResume(false);
        }

        FreelancerProfile freelancerProfile = application.getFreelancerProfile();
        if (freelancerProfile != null) {
            response.setFreelancerId(freelancerProfile.getId());
            response.setFreelancerFirstName(freelancerProfile.getFirstName());
            response.setFreelancerLastName(freelancerProfile.getLastName());
            response.setDesiredPay(application.getDesiredPay());
            response.setFormattedApplicationTime(ApplicationTimeFormatter.formatApplicationTime(application.getApplicationTime()));
            response.setFreelancerEmail(freelancerProfile.getUser().getEmail());
        }
        return response;
    }
}