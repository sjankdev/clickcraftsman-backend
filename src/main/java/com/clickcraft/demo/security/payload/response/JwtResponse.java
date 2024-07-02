package com.clickcraft.demo.security.payload.response;

import lombok.Getter;

import java.util.List;

@Getter
public class JwtResponse {

    private final String accessToken;
    private final String tokenType = "Bearer";
    private final Long id;
    private final String email;
    private final List<String> roles;

    public JwtResponse(String accessToken, Long id, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }
}
