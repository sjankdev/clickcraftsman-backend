package com.clickcraft.demo.service.impl;

import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.repository.SkillRepository;
import com.clickcraft.demo.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

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
   