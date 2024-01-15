package com.clickcraft.demo.service;

import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public List < Skill > getSkillsByIds(List < Long > skillIds) {
        if (skillIds == null) {
            return Collections.emptyList();
        }

        List < Long > validSkillIds = skillIds.stream().filter(id -> id != null && id > 0).collect(Collectors.toList());

        if (validSkillIds.isEmpty()) {
            return Collections.emptyList();
        }

        return skillRepository.findAllById(validSkillIds);
    }

    public List < Skill > getSkillsByNames(List < String > skillNames) {
        if (skillNames == null || skillNames.isEmpty()) {
            return Collections.emptyList();
        }

        return skillRepository.findBySkillNameIn(skillNames);
    }

    public List < Skill > getAllSkills() {
        return skillRepository.findAll();
    }
}