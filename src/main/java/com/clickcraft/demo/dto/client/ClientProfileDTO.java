package com.clickcraft.demo.dto.client;

import com.clickcraft.demo.models.User;
import com.clickcraft.demo.models.enums.ELocations;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientProfileDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String contactPhone;
    private ELocations location;
    private byte[] profilePictureData;
    private String companyName;
    private String companyLocation;
    private String companySize;
    private String companyIndustry;
    private String linkedin;
    private String website;
    private String instagram;

    public static ClientProfileDTO fromUser(User user) {
        ClientProfileDTO clientProfileDTO = new ClientProfileDTO();

        if (user != null) {
            if (user.getClientProfile() != null) {
                clientProfileDTO.setFirstName(user.getClientProfile().getFirstName());
                clientProfileDTO.setLastName(user.getClientProfile().getLastName());
                clientProfileDTO.setContactPhone(user.getClientProfile().getContactPhone());
                clientProfileDTO.setLocation(user.getClientProfile().getLocation());
                clientProfileDTO.setCompanyName(user.getClientProfile().getCompanyName());
                clientProfileDTO.setCompanyLocation(user.getClientProfile().getCompanyLocation());
                clientProfileDTO.setCompanyIndustry(user.getClientProfile().getCompanyIndustry());
                clientProfileDTO.setCompanySize(user.getClientProfile().getCompanySize());
                clientProfileDTO.setInstagram(user.getClientProfile().getInstagram());
                clientProfileDTO.setWebsite(user.getClientProfile().getWebsite());
                clientProfileDTO.setLinkedin(user.getClientProfile().getLinkedin());
                clientProfileDTO.setProfilePictureData(user.getProfilePictureData());
            }
        }
        return clientProfileDTO;
    }
}