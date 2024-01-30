package com.clickcraft.demo.service;

import com.clickcraft.demo.models.Skill;

import java.util.List;

public interface SkillService {
    List<Skill> getSkillsByIds(List<Long> skillIds);

    List<Skill> getSkillsByNames(List<String> skillNames);

    List<Skill> getAllSkills();
}
