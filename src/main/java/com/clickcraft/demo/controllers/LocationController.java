package com.clickcraft.demo.controllers;

import com.clickcraft.demo.models.ELocations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @GetMapping("/getAllLocations")
    public ResponseEntity < List < String >> getAllLocations() {
        List < String > locations = Arrays.stream(ELocations.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        return ResponseEntity.ok(locations);
    }
}