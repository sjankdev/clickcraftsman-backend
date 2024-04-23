package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.repository.SkillRepository;
import com.clickcraft.demo.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Autowired
    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public List<Skill> getSkillsByNames(List<String> skillNames) {
        if (skillNames == null || skillNames.isEmpty()) {
            return Collections.emptyList();
        }

        return skillRepository.findBySkillNameIn(skillNames);
    }

    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }
}
