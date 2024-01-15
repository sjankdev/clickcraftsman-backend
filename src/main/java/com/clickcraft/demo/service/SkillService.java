package com.clickcraft.demo.service;

import com.clickcraft.demo.models.Skill;
import com.clickcraft.demo.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public List<Skill> getSkillsByIds(List<Long> skillIds) {
        return skillRepository.findAllById(skillIds);
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }
}
