package com.clickcraft.demo.controllers;

import com.clickcraft.demo.models.enums.ELocations;
import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/utils")
@RequiredArgsConstructor
public class UtilsController {

    private final SkillService skillService;

    @GetMapping("/getAllSkills")
    public List<Skill> getAllSkills() {
        return skillService.getAllSkills();
    }

    @GetMapping("/getAllLocations")
    public ResponseEntity<List<String>> getAllLocations() {
        List<String> locations = Arrays.stream(ELocations.values()).map(Enum::name).collect(Collectors.toList());

        return ResponseEntity.ok(locations);
    }
}