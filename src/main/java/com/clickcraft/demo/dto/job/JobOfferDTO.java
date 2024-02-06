package com.clickcraft.demo.dto.job;

import com.clickcraft.demo.models.JobOffer;

import java.time.LocalDateTime;

public class JobOfferDTO {
    private Long id;
    private LocalDateTime offerDate;
    private String messageToFreelancer;

    public static JobOfferDTO fromJobOffer(JobOffer jobOffer) {
        JobOfferDTO jobOfferDTO = new JobOfferDTO();
        jobOfferDTO.setId(jobOffer.getId());
        jobOfferDTO.setOfferDate(jobOffer.getOfferDate());
        jobOfferDTO.setMessageToFreelancer(jobOffer.getMessageToFreelancer());
        return jobOfferDTO;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(LocalDateTime offerDate) {
        this.offerDate = offerDate;
    }

    public String getMessageToFreelancer() {
        return messageToFreelancer;
    }

    public void setMessageToFreelancer(String messageToFreelancer) {
        this.messageToFreelancer = messageToFreelancer;
    }
}
